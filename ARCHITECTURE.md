# Social Group Simulator - Architecture Overview

## Current Architecture

Package layout (src/main/java/org/campusboard/sgs):
- Main.java — application entry point; wires repos, controllers, DemoDataSeeder, and MainWindow.
- controller — AuthController (login/logout), PostController (posts, filters/sort/search, likes, undo/redo), Controller façade for legacy call sites, Command/UndoManager + create/edit/delete/like commands.
- controller — AuthController (login/logout), PostController (posts, filters/sort/search, likes, undo/redo), CommentController (list/add comments), Controller façade for legacy call sites, Command/UndoManager + create/edit/delete/like commands.
- model — Role, UserType, User, Category (ANNOUNCEMENTS, STUDY_GROUPS, EVENTS, LOST_FOUND), Post (per-user likes set, createdAt, author string + validation), Comment.
- repo — PostRepository + InMemoryPostRepository; UserRepository + InMemoryUserRepository (seeded users in Main); CommentRepository + InMemoryCommentRepository.
- filter — FilterStrategy and implementations (AllFilter, CategoryFilter, AuthorTypeFilter, TrendingFilter, SortByNew, AuthorFilter).
- util — Session, Events enum, EventBus, IconLoader.
- view — MainWindow, TopBar, SidebarPanel, FeedPanel, PostCard, dialogs (Create/Manage/Moderate/Reports, PostDetailDialog).
- demo — DemoDataSeeder ensures 40–60 realistic posts (≥10 per category) only when the board is empty.

Key design points
- Command pattern: controller/*Command.java + UndoManager support undo/redo for create/edit/delete/like.
- Simple models: Post stores author as String; likes tracked per user; createdAt captured; Category limited to four values.
- Event bus: util.Events + util.EventBus (no legacy controller.AppEvent) for UI refresh and auth events.
- Strategy filters/sort: FilterStrategy drives filtering and ordering; PostController holds current filter/sort/search.
- UI wiring: Controllers drive repositories; EventBus notifies TopBar/SidebarPanel/FeedPanel/PostCard to refresh without full reloads when possible.
- Seeding: DemoDataSeeder runs at startup (if empty) to populate varied demo posts, including [TEST] edge-case content.

Build/structure sanity checklist
- Uses PostController/AuthController (no monolithic Controller.java for events).
- Data layer under repo/ (no Persistence/ package).
- Events via util.Events + util.EventBus.
- Post uses String author and toggleLike(String userId) with a per-user set.
- Categories limited to ANNOUNCEMENTS, STUDY_GROUPS, EVENTS, LOST_FOUND.
- Resources under src/main/resources/icons/**.
- Tests live in src/test/java/org/campusboard/sgs/**; `./gradlew test` passes on a clean checkout.

Filters and likes (concept)
- FilterStrategy interface enables composable filtering/sorting; PostController.current() applies search, filter, then sort.
- Per-user likes enforced in Post.toggleLike(String userId); LikePostCommand publishes POST_UPDATED for lightweight UI refresh.

Build/Run quick start
- Build: `./gradlew clean build`
- Run: `./gradlew run`
- Test: `./gradlew test`
