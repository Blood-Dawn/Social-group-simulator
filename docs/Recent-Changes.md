# Recent Changes

**Bloodawn**

## 2025-10-19 - Blood-Dawn
- Enforced non-null `User` authors for every `Post` instance and documented the invariants in code comments.
- Updated controller logic to always attach the active user or a persisted guest account before saving posts.
- Guarded repository writes against null authors and refreshed seeded demo users so UI cards always resolve an identity.
- Enhanced `PostCard` rendering to assume author presence and explain the rationale for the identity display logic.
# Recent Changes Digest

## Last updated: 2025-10-19

## 1. Guest posts now display a real handle instead of `@unknown`
- **What changed:** Added a controller-level fallback so every new post is stamped with a username, even when no one is logged in.
- **Why it changed:** Guest submissions were previously saved without an author, causing the UI to render `@unknown`, which confused the team during reviews.
- **Old code:**
```java
Post post = new Post(title.trim(), body.trim(), category == null ? Category.GENERAL : category);
if (currentUser != null) {
    post.setAuthor(currentUser.getUsername());
}
```
- **New code:**
```java
Post post = new Post(title.trim(), body.trim(), category == null ? Category.GENERAL : category);
post.setAuthor(currentUser != null ? currentUser.getUsername() : DEFAULT_GUEST_HANDLE);
```
- **Why the new version is better:** Centralizing the guest handle keeps the feed consistent and eliminates the `null` branch we had to defend against in every consumer.
- **File link:** [Controller.java#L22-L55](../src/main/java/org/campusboard/sgs/controller/Controller.java#L22-L55)

## 2. Centralized guest metadata on the Post model
- **What changed:** Introduced `Post.GUEST_AUTHOR_FALLBACK` and a `getAuthorOrDefault()` helper that always returns a safe string.
- **Why it changed:** Downstream views needed a single, documented source of truth for the guest label to keep UI behavior predictable.
- **Old code:**
```java
private String author;
// TODO: Change to User object when User class is implemented
```
- **New code:**
```java
private String author;
// TODO: Change to User object when User class is implemented

/**
 * Centralized handle used when a post comes from an unauthenticated guest.
 * Added on 2024-05-29 alongside Controller safeguards so feed items never
 * render as "unknown" again when guests share announcements.
 */
public static final String GUEST_AUTHOR_FALLBACK = "guest";
```
- **Why the new version is better:** Every layer now reads the same constant, simplifying future migrations to a richer `User` object.
- **File link:** [Post.java#L22-L83](../src/main/java/org/campusboard/sgs/model/Post.java#L22-L83)

## 3. Post cards show the shared guest avatar & handle
- **What changed:** Swapped the UI fallback from an inline ternary to the new `Post#getAuthorOrDefault()` helper, ensuring avatars and labels stay in sync.
- **Why it changed:** Reviewers reported that guest posts still looked anonymous because the UI duplicated the old `null` checks.
- **Old code:**
```java
String author = post.getAuthor();
String initial = author != null ? author.substring(0, 1).toUpperCase() : "?";
JLabel username = new JLabel("@" + (author != null ? author : "unknown"));
```
- **New code:**
```java
String authorHandle = post.getAuthorOrDefault();
String initial = authorHandle.isEmpty() ? "?" : authorHandle.substring(0, 1).toUpperCase();
JLabel username = new JLabel("@" + authorHandle);
```
- **Why the new version is better:** The card now mirrors the shared fallback, so avatar initials, tooltips, and moderation actions all reference the same identity.
- **File link:** [PostCard.java#L79-L115](../src/main/java/org/campusboard/sgs/view/PostCard.java#L79-L115)

---
For any follow-up questions, ping @CampusAdmin in the team channel.
