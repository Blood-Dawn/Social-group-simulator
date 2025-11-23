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

Where to change things quickly
- Adjust seeds: `Main.seedUsers` / `Main.seedPosts`.
- Tweak filters/sort: `filter/` package and `PostController`.
- UI tweaks: `view/TopBar`, `view/SidebarPanel`, `view/FeedPanel`, `view/PostCard`.
