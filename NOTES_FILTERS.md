CampusBoard filtering, sorting, and likes (current code understanding)

- Filters use the `FilterStrategy` interface: UI components (e.g., `SidebarPanel`) set the active strategy on `PostController`, which then filters the in-memory posts before rendering in `FeedPanel`.
- Sorting today is implicit: posts default to newest-first (createdAt desc). Additional sort strategies (e.g., trending by like count, “SortByNew”) can be plugged in alongside filters.
- Search comes from `TopBar` and flows through `PostController.setSearch`, which publishes `SEARCH_CHANGED`; `FeedPanel` refreshes accordingly.
- Likes are per-user: `Post.toggleLike(userId)` flips membership in a liked set, updates counts, and emits `POST_UPDATED` via `LikePostCommand`; `FeedPanel` reacts without wiping scroll position.
- EventBus drives refreshes: `POSTS_REPLACED` for broad changes (create/delete), `POST_UPDATED` for targeted changes (like/edit), plus `FILTER_CHANGED`/`SEARCH_CHANGED` to rebuild filtered/sorted views.
