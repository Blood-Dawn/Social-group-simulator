# MVP Polish Sprint - Focused Implementation Plan

**Branch**: `fix/mvp-polish` (off main)
**Target**: Complete remaining MVP features without changing what works

---

## What's Already Done (Don't Touch)

- ✅ Like toggle logic (Post.toggleLike)
- ✅ Scroll preservation (FeedPanel)
- ✅ User seeding (4 users with passwords)
- ✅ Lost & Found category
- ✅ Theme enums (CategoryTheme, RoleTheme)
- ✅ IconLoader utility
- ✅ Centralized permissions (controller.canModifyPost)
- ✅ HTML label wrapping

---

## What Needs Implementation

### 1. Admin Dialogs - Real Functionality
**Files**: ManageUsersDialog, ModeratePostsDialog, ViewReportsDialog

Currently these dialogs exist but don't do anything. Need to wire them up.

#### 1a. ManageUsersDialog
- Display list of users from repository
- Toggle user active/inactive status
- Save changes back to repository

#### 1b. ModeratePostsDialog
- Display all posts with counts
- Add "Hide/Unhide" toggle
- Hidden posts filter out of feed

#### 1c. ViewReportsDialog
- Display statistics: total posts, total users, posts per category
- Show recent activity
- List most liked posts

---

### 2. Icon Integration
**Goal**: Replace emoji placeholders with actual icons

**Phase A**: Download/Create Icons
- Get icons from Material Design Icons (recommended)
- Need: like-outline, like-filled, delete, search (minimum)
- Place in: src/main/resources/icons/actions/ and icons/ui/

**Phase B**: Wire Icons to UI
- PostCard: Replace ♥ emoji with icon (line 140)
- PostCard: Replace 🗑 emoji with icon (line 155)
- TopBar: Replace 🔍 emoji with icon (line 103)
- Remove TODO comments

**Implementation**:
```java
// PostCard - Like button with icon states
ImageIcon likeOutline = IconLoader.loadAction("like-outline", 16);
ImageIcon likeFilled = IconLoader.loadAction("like-filled", 16);

// In bind():
boolean liked = post.isLikedBy(session.user().username());
likeButton.setIcon(liked ? likeFilled : likeOutline);
likeButton.setText(String.valueOf(post.likeCount()));
```

---

### 3. Make Sidebar Collapsible
**File**: MainWindow.java

Add one-touch collapse to JSplitPane:
```java
JSplitPane split = new JSplitPane(...);
split.setOneTouchExpandable(true); // ADD THIS LINE
split.setDividerLocation(260);
```

---

### 4. Add "(test)" Tags to Seed Posts
**File**: Main.java

Mark seeded posts with (test) prefix:
```java
posts.save(new Post(null,
    "(test) Welcome to CampusBoard!",  // Add (test) prefix
    "This is your campus social hub...",
    Category.ANNOUNCEMENTS,
    "admin"));
```

---

### 5. Unit Tests
**New Files**: src/test/java/org/campusboard/sgs/

#### 5a. AuthControllerTest.java
```java
@Test void validLogin_setsUserRole() {
  AuthController auth = new AuthController(users, session, bus);
  assertTrue(auth.login("admin", "admin123"));
  assertEquals(Role.ADMIN, session.role());
}

@Test void invalidPassword_rejectsLogin() {
  assertFalse(auth.login("admin", "wrong"));
}
```

#### 5b. PostToggleLikeTest.java
```java
@Test void toggleLike_firstClick_increments() {
  Post post = new Post(null, "Test", "Body", Category.EVENTS, "student");
  assertTrue(post.toggleLike("user1")); // liked
  assertEquals(1, post.likeCount());
}

@Test void toggleLike_secondClick_decrements() {
  post.toggleLike("user1");
  assertFalse(post.toggleLike("user1")); // unliked
  assertEquals(0, post.likeCount());
}
```

#### 5c. FilterTest.java
```java
@Test void categoryFilter_returnsOnlyMatchingPosts() {
  FilterStrategy filter = new CategoryFilter(Category.EVENTS);
  List<Post> filtered = filter.filter(allPosts).toList();
  assertTrue(filtered.stream().allMatch(p -> p.category() == Category.EVENTS));
}
```

---

## Implementation Order

**Priority 1** (Must Have):
1. Make sidebar collapsible (5 minutes)
2. Add "(test)" tags to seed posts (5 minutes)
3. Wire up admin dialog basic functionality (1-2 hours)

**Priority 2** (Should Have):
4. Download and integrate icons (1 hour)
5. Write unit tests (2 hours)

**Priority 3** (Nice to Have):
6. Enhanced icon states (like filled/outline toggle)
7. Additional test coverage

---

## Answers to Questions

**Q1**: Do you want guest likes allowed, or force login first?
**A**: Current code already requires login (PostController.toggleLike checks session.isAuthenticated)

**Q2**: Should "Lost & Found" be a category and a tag, or just a category?
**A**: It's already just a category (Category.LOST_FOUND) - no changes needed

**Q3**: Want me to prep the exact file list to restore from the last "good" commit on main?
**A**: Not needed - current branch has all good changes, just needs these additions

---

## Success Criteria

- [ ] Admin dialogs show real data and make changes
- [ ] Sidebar can collapse/expand
- [ ] Seeded posts have "(test)" prefix
- [ ] At least 3 emoji icons replaced with PNG/SVG
- [ ] 5+ unit tests pass
- [ ] No scroll jumping on like
- [ ] Login works with seeded credentials

---

## What NOT to Change

- Don't modify FeedPanel scroll preservation logic
- Don't change Post.toggleLike() signature
- Don't alter CategoryTheme or RoleTheme enums
- Don't restructure icon directories
- Don't change existing permission logic
- Don't modify seeded user credentials

---

*This plan focuses only on incomplete features. Everything else is working and should not be modified.*
