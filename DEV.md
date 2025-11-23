Development guide (CampusBoard)

Tooling
- Java: toolchain set to 21 via Gradle (build.gradle). Uses Gradle 8.x wrapper.
- Entry point: `org.campusboard.sgs.Main`.
- Build: `./gradlew clean build` (compile + tests). Tests only: `./gradlew test`. Kill stale daemons: `./gradlew --stop`.
- Run app: `./gradlew run` launches Swing UI with in-memory seed data (users + posts).
- Faster iteration: `./gradlew classes --continuous` to recompile on save; restart the app to pick up changes (no hot-reload for Swing).

Architecture
- MVC + EventBus (see `ARCHITECTURE.md` for details).
- Controllers: `PostController` (posts, filters/sort/search, likes, undo/redo), `AuthController` (login/logout), `CommentController` (list/add comments), `Controller` façade for older API calls.
- Models: `Post` (per-user likes, createdAt, category, author string), `Category`, `Role`/`UserType`, `User`, `Comment`.
- Repos: `InMemoryPostRepository`, `InMemoryUserRepository`, `InMemoryCommentRepository`.
- Filters/sort: strategy interface `filter/FilterStrategy` with `AllFilter`, `CategoryFilter`, `AuthorTypeFilter`, `TrendingFilter`, `SortByNew`, etc.
- Events: `util.EventBus` + `Events` enum (POSTS_REPLACED, POST_UPDATED, COMMENTS_CHANGED, FILTER_CHANGED, SEARCH_CHANGED, USER_LOGGED_IN, USER_LOGGED_OUT, SHOW_LOGIN).
- Views: `MainWindow` hosts `TopBar`, `SidebarPanel`, `FeedPanel`; `PostCard` renders each post; `PostDetailDialog` shows full post + comments; dialogs under `view/dialogs`.

Seeds / demo data
- Users (see `Main.seedUsers`): guest/guest123, student/student123, staff/staff123, admin/admin123.
- Posts: populated via `DemoDataSeeder.ensureDemoData` when empty (40–60 realistic posts, 10+ per category with [TEST] edge cases). Admin-authored seeded posts are forced to start with “[TEST]”. `InMemoryPostRepository` no longer seeds by itself.
- Comments: `DemoDataSeeder.ensureDemoComments` seeds sample comments across posts when empty.
- Reset/repeatability: data is in-memory; every app restart reseeds. Seeding uses a random seed per run unless you pin `-Dsgs.demo.seed=12345` to make runs deterministic (tests pin seeds explicitly).

Icons and resources
- Location: `src/main/resources/icons`.
- Categories: all, announcements, events, lost-found, study-groups, trending.
- UI: `ui/CB_Icon.png` is the top-left badge; other UI icons include search, clock, user avatars.
- If icons appear as boxes, verify file names/case and that Gradle includes resources (default resource dir already configured).

Filters, sort, search, likes (behavior)
- Active filter + sort stored on `PostController`; UI sets strategies via `SidebarPanel`.
- Search text from `TopBar` → `PostController.setSearch` → `Events.SEARCH_CHANGED`.
- Likes: `Post.toggleLike(userId)` maintains a per-user set; `LikePostCommand` publishes `POST_UPDATED` so `FeedPanel` can rebind without scroll jump.
- Feed refresh: `POSTS_REPLACED` (create/delete) triggers rebuild; `POST_UPDATED` updates in-place when sort is stable (newest-first).

Undo/redo
- Commands in `controller/*Command.java` with `UndoManager`. Accessible via Edit menu (Ctrl+Z/Ctrl+Y) and controller methods.

UI shortcuts and dialogs
- Logout/delete confirmations support Y/N mnemonics (Shift+Y/Shift+N).
- Menu: Ctrl+N opens Create Post dialog; admin menu shows for Role.ADMIN.

Testing
- JUnit 5 in `src/test/java/org/campusboard/sgs/**`.
- Coverage includes filters, controllers, model likes, auth, IconLoader. Run `./gradlew test`.

Common issues / troubleshooting
- Icons missing: ensure names match loader calls (see `SidebarPanel.loadCategoryIcon`) and resources are under `src/main/resources/icons/**`.
- Scroll speed: adjustable via `FeedPanel` scrollbar unit increment.
- No hot reload: restart after code changes; use continuous compile to speed feedback.
- Gradle cache/daemon hiccups: `./gradlew --stop` then rebuild; delete `.gradle` if corrupted.

Recent hardening (high level)
- Post model: Jackson @JsonCreator restored; enforced non-blank title/body; guest fallback normalized; per-user likes preserved.
- UI polish: PostCard heart icons use explicit escapes + tooltips; sidebar guest label wraps; scroll speed increased; logout/delete dialogs support Y/N mnemonics; CB_Icon.png used in the header.
- Icons: sidebar avoids placeholder squares; uses real assets where present.
- Docs: NOTES_FILTERS.md documents current filter/sort/search/like flow; tests run via `./gradlew test`.
- Comments & detail: Comment model/repo/controller added; PostDetailDialog shows full post + comments with add-comment flow; PostCard opens detail; demo seeding adds comments to ~45% posts.
- Seeding rules: Admin demo posts are forced "[TEST]" and hidden from non-admin users; staff/student posts drop the prefix. Authors now use human-like handles (student/staff/admin/guest) and are seeded as users; comments seeded with variable counts. Seed randomized per run unless `-Dsgs.demo.seed` is set; restart resets data.
- Validation: PostController create/edit now rejects blank title/body; event tests verify POST_UPDATED/POSTS_REPLACED; admin visibility enforced for admin-authored posts/comments.
- Tests: Added coverage for seeder idempotence, admin/staff title rules, comment seeding, comment controller, and visibility rules; test suite remains green.
- Filters & refresh: Sidebar filters now combine category + author type; active filters highlight with a blue border and toggle off on re-click; "All Posts" clears both. Author labels show role tags via UserRepository lookup. Added TopBar refresh button to rebuild feed without navigation.
- Auth data: Demo users seeded for all demo authors (students/staff/admin/guest) so author-type filters work; InMemoryUserRepository hashes passwords and supports validate/assign.
- Manage Users: Dialog now lists all users (baseline + seeded) and role toggles update the repository-backed list.

Completed TODOs (details)
- Demo data & comments: Added `DemoDataSeeder` (posts/comments seeding on empty repos, admin posts forced “[TEST]”, staff not; human-like authors; seeded comments on ~45% posts). Wired into `Main` startup with optional seed override (`-Dsgs.demo.seed`). Added Comment entity/repo/controller plus PostDetailDialog (full post + comments + add-comment) and PostCard “Open” action to launch it without full feed rebuild.
- Data validation: PostController now guards create/edit with non-blank checks to prevent invalid posts (controller layer, ensures safer inputs).
- Comments model/repo hardening: Added Comment entity with validation, in-memory repo, and controller as the sole access point for listing/adding comments.
- Post detail UI: Introduced PostDetailDialog to show full post + comments and allow adding comments; wired from PostCard without forcing feed refresh.
- Demo comments seeding: Seeder now attaches comments (1–5) to ~45% of posts, uses real post IDs, and enforces “[TEST]” on admin while keeping staff posts non-[TEST].
- Tests: Added idempotence check for seeding, admin/staff title rules, comment repo/controller coverage, and validation/POST_UPDATED event checks to keep behaviors stable.
- Icons: Verified real category/sort/author icons in `src/main/resources/icons/**`; sidebar no longer uses placeholders or colored boxes.
- UI smoke: Performed manual run to exercise login, filters/sort, like/unlike, and guest visibility; feed kept scroll position under interactions.
- Password handling: InMemoryUserRepository now hashes passwords (SHA-256) and exposes validate/assign; AuthController delegates validation to repo.
- Broader tests: Added PostController event tests for POST_UPDATED/POSTS_REPLACED and blank input validation; overall test suite remains passing.

Where to change things quickly
- Adjust seeds: `Main.seedUsers` / `Main.seedPosts`.
- Tweak filters/sort: `filter/` package and `PostController`.
- UI tweaks: `view/TopBar`, `view/SidebarPanel`, `view/FeedPanel`, `view/PostCard`.
