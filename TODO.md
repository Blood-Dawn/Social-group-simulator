## Demo Data & Comments Feature

- [x] Seed robust demo board data (10+ posts per category)
  - [x] Add DemoDataSeeder with deterministic templates (authors, avatars, titles/bodies per category)
  - [x] Only seed when repositories are empty
  - [x] Wire seeder into Main startup after repos init
  - [x] Include [TEST] long/edge-case posts per category
- [x] Add simple Comment model & repository
  - [x] Comment entity (id, postId, author, body, createdAt)
  - [x] In-memory CommentRepository with seed data
  - [x] Controller methods for listing/adding comments
  - [x] Tests for comment add/list invariants
- [x] Add Post Detail UI with comments view
  - [x] Detail dialog/panel showing full post + comments list
  - [x] Allow adding a new comment (authenticated users)
- [x] Wire click-through from feed cards to detail view
  - [x] PostCard open-action to launch detail view (Open button)
  - [x] EventBus or direct controller hook to fetch comments
- [x] Add tests for demo data + comments
  - [x] Seeder ensures >=10 per category, mixed authors/avatars
  - [x] Detail view shows seeded comments and new comment append (covered via controller tests)


