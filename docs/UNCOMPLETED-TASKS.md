# Campus Board - Uncompleted Tasks
**Work Remaining & Implementation Priorities**

**Last Updated**: November 12, 2025
**Total Remaining**: 17 items (enhancements and polish)

---

## 🚨 URGENT - Post-Launch Polish (9 Items)
**Priority**: HIGH
**Target**: Week of November 12-19, 2025
**Owner**: Frontend Team

These items improve user experience and security. They should be addressed before the next demo or release.

---

### 1. Admin-Only Post Deletion
**Priority**: 🔴 **CRITICAL**
**Effort**: Medium (2-3 hours)
**Status**: Not Started

**Description**:
Restrict the delete button visibility and functionality to only:
- Post author (owner)
- STAFF role users
- ADMIN role users

**Current Behavior**:
- Delete button visible to all users on all posts

**Target Behavior**:
- Button only visible when `canModifyPost(post)` returns true
- `canModifyPost()` checks: `post.author().equals(session.user().username()) || session.role() == Role.STAFF || session.role() == Role.ADMIN`

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/PostCard.java:106`
  - Add conditional visibility: `deleteButton.setVisible(canDelete(post))`
  - Create `canDelete(Post post)` method checking session role and authorship

**Acceptance Criteria**:
- [ ] Students cannot see delete button on others' posts
- [ ] Students can see delete button on own posts
- [ ] Staff can delete any post
- [ ] Admin can delete any post
- [ ] Guests cannot delete any posts

---

### 2. Staff Permission Alignment
**Priority**: 🔴 **CRITICAL**
**Effort**: Small (1 hour)
**Status**: Not Started

**Description**:
Ensure STAFF role has same permissions as STUDENT role (no special privileges beyond STUDENT capabilities).

**Current Behavior**:
- Staff may have elevated privileges in some code paths

**Target Behavior**:
- STAFF = STUDENT permissions
- Only ADMIN has elevated privileges

**Files to Review**:
- `src/main/java/org/campusboard/sgs/controller/PostController.java:105-110`
  - Update `canModifyPost()` to treat STAFF same as STUDENT
  - Remove any STAFF-specific privilege checks

**Acceptance Criteria**:
- [ ] Staff cannot moderate posts
- [ ] Staff cannot manage users
- [ ] Staff can only edit/delete own posts
- [ ] Admin menu hidden for staff

---

### 3. Admin-Only Sidebar Tools
**Priority**: 🔴 **CRITICAL**
**Effort**: Small (1 hour)
**Status**: Not Started

**Description**:
Hide the Admin menu in the menu bar for non-admin users.

**Current Behavior**:
- Admin menu visible to all users (though actions may fail)

**Target Behavior**:
- Admin menu only visible when `session.role() == Role.ADMIN`

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/MainWindow.java:98`
  - Already implemented! `adminMenu.setVisible(isAdmin)`
  - Verify this is working correctly

**Acceptance Criteria**:
- [x] Admin menu hidden for GUEST users (already implemented)
- [x] Admin menu hidden for STUDENT users (already implemented)
- [x] Admin menu hidden for STAFF users (already implemented)
- [x] Admin menu visible for ADMIN users (already implemented)

**Status**: ✅ **VERIFY ONLY** - Implementation appears complete, just need testing confirmation

---

### 4. Role-Based UI Theming
**Priority**: 🟡 **MEDIUM**
**Effort**: Large (4-6 hours)
**Status**: Not Started

**Description**:
Introduce unique UI themes or visual indicators per user role for clearer differentiation.

**Proposed Changes**:
- **GUEST**: Gray/neutral theme, limited controls
- **STUDENT**: Blue theme (FAU colors), standard controls
- **STAFF**: Green theme, standard controls
- **ADMIN**: Red theme (FAU red), admin controls visible

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/MainWindow.java`
  - Add `applyTheme(Role role)` method
  - Call on login/logout events
- `src/main/java/org/campusboard/sgs/view/TopBar.java`
  - Update background color based on role
- `src/main/java/org/campusboard/sgs/view/PostCard.java`
  - Add role badge to user avatar

**Acceptance Criteria**:
- [ ] Each role has distinct color scheme
- [ ] Theme changes on login/logout
- [ ] Consistent theming across all components
- [ ] Role badge visible on post cards

---

### 5. Preserve Scroll Position on Like
**Priority**: 🟢 **LOW** (Already Implemented!)
**Effort**: N/A
**Status**: ✅ **COMPLETE**

**Description**:
Prevent feed from jumping to top when user likes a post.

**Current Implementation**:
- `src/main/java/org/campusboard/sgs/view/FeedPanel.java:38-60`
- Line 39: `int y = scroll.getVerticalScrollBar().getValue();` - remembers position
- Line 60: `SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(y));` - restores position

**Acceptance Criteria**:
- [x] Like action preserves scroll position
- [x] POST_UPDATED event triggers differential refresh
- [x] Only changed PostCard updates (not full rebuild)

**Status**: ✅ **COMPLETE** - No action needed

---

### 6. Single-Like Enforcement
**Priority**: 🟢 **LOW** (Already Implemented!)
**Effort**: N/A
**Status**: ✅ **COMPLETE** (Verify UI Feedback)

**Description**:
Enforce single-like interactions: first tap likes, second tap removes like, block duplicate reactions.

**Current Implementation**:
- `src/main/java/org/campusboard/sgs/model/Post.java:50-59`
  - `toggleLike(String userId)` returns true if liked, false if unliked
  - Uses `Set<String> likedBy` to prevent duplicates
  - `likedBy.remove(userId)` on second click

**UI Feedback**:
- `src/main/java/org/campusboard/sgs/view/PostCard.java:52`
  - Like button shows current count: `likeButton.setText("♥ " + post.likeCount())`

**Potential Enhancement**:
- Change like button color when user has liked (red vs gray)
- Add "You liked this" tooltip

**Acceptance Criteria**:
- [x] First click adds like
- [x] Second click removes like
- [x] No duplicate likes from same user
- [ ] Visual feedback shows if user has liked (enhancement)

**Status**: ✅ **FUNCTIONAL** - Enhancement for visual feedback recommended

---

### 7. Dislike Button Implementation
**Priority**: 🟡 **MEDIUM**
**Effort**: Medium (2-3 hours)
**Status**: Not Started

**Description**:
Add a dislike button next to like button with proper toggling behavior.

**Proposed Design**:
- Like and dislike are mutually exclusive
- Clicking dislike removes like (if present) and adds dislike
- Clicking like removes dislike (if present) and adds like
- Second click on same button removes reaction

**Files to Modify**:
1. **Post.java**:
   ```java
   private final Set<String> likedBy = new HashSet<>();
   private final Set<String> dislikedBy = new HashSet<>();

   public boolean toggleDislike(String userId) {
     likedBy.remove(userId); // Remove like if present
     if (dislikedBy.remove(userId)) return false; // Already disliked
     dislikedBy.add(userId); // Add dislike
     return true;
   }

   public int dislikeCount() { return dislikedBy.size(); }
   public boolean isDislikedBy(String userId) { return dislikedBy.contains(userId); }
   ```

2. **PostController.java**:
   ```java
   public void toggleDislike(Post p) {
     if (!session.isAuthenticated()) {
       bus.publish(Events.SHOW_LOGIN, null);
       return;
     }
     undoManager.execute(new DislikePostCommand(posts, bus, p, session.user().username()));
   }
   ```

3. **DislikePostCommand.java** (new file):
   ```java
   public class DislikePostCommand implements Command {
     // Similar to LikePostCommand
   }
   ```

4. **PostCard.java**:
   - Add dislike button next to like button
   - Wire to `controller.toggleDislike(post)`
   - Update counts on POST_UPDATED event

**Acceptance Criteria**:
- [ ] Dislike button displays count
- [ ] Clicking dislike removes like (if present)
- [ ] Clicking like removes dislike (if present)
- [ ] Second click removes dislike
- [ ] Undo/redo support via Command pattern
- [ ] Guests prompted to login

---

### 8. Icon Assets
**Priority**: 🟡 **MEDIUM**
**Effort**: Small (1-2 hours)
**Status**: Not Started

**Description**:
Replace placeholder text and emoji icons with finalized graphic assets.

**Icons Needed**:
1. **Like icon**: Heart shape (outlined and filled states)
2. **Dislike icon**: Thumbs down (outlined and filled states)
3. **Delete icon**: Trash can
4. **Edit icon**: Pencil
5. **Search icon**: Magnifying glass (currently using emoji 🔍)
6. **Filter icons**: For each category
   - Announcements: Megaphone
   - Study Groups: Book/Group
   - Events: Calendar
   - Lost & Found: Key/Magnifying glass
7. **Login icon**: User silhouette
8. **Logout icon**: Door/Arrow

**Recommended Format**:
- PNG with transparency
- SVG for scalability
- 16x16, 24x24, 32x32 sizes
- Consistent style (Material Design or Font Awesome)

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/PostCard.java`
  - Replace "♥" with icon
- `src/main/java/org/campusboard/sgs/view/TopBar.java`
  - Replace "🔍" with icon
- `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`
  - Add icons to filter buttons

**Resources Directory**:
- Create: `src/main/resources/icons/`

**Acceptance Criteria**:
- [ ] All placeholder emojis replaced
- [ ] Consistent icon style across app
- [ ] Icons scale properly on different displays
- [ ] Icons support light/dark themes

---

### 9. Circular Logo Badge
**Priority**: 🟢 **LOW**
**Effort**: Small (30 minutes)
**Status**: Not Started

**Description**:
Update the top-left "F" logo to render inside a circular badge for a sleeker look.

**Current Implementation**:
- No logo visible in current implementation

**Proposed Implementation**:
- Add circular badge with "CB" (CampusBoard) or "F" (FAU) inside
- Place in top-left corner of MainWindow
- Size: 40x40 pixels
- Background: FAU Navy (#003366)
- Text: White, bold, centered

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/MainWindow.java`
  - Add logo panel to north section
- OR `src/main/java/org/campusboard/sgs/view/TopBar.java`
  - Add logo to left side of TopBar

**Acceptance Criteria**:
- [ ] Circular badge visible
- [ ] Professional appearance
- [ ] Does not interfere with other UI elements

---

## 🔧 MEDIUM PRIORITY - Enhancements (4 Items)
**Priority**: MEDIUM
**Target**: December 2025
**Owner**: Backend Team

---

### 10. SearchService.java
**Priority**: 🟡 **MEDIUM**
**Effort**: Large (4-6 hours)
**Status**: Not Started

**Description**:
Create advanced search service with field-specific queries and combined criteria.

**Location**: `src/main/java/org/campusboard/sgs/service/SearchService.java`

**Features**:
1. **Field-Specific Search**:
   - Search by title only
   - Search by body only
   - Search by author only
   - Search by category

2. **Advanced Search**:
   - Boolean operators (AND, OR, NOT)
   - Phrase matching ("exact phrase")
   - Wildcard support (title:*campus*)
   - Regular expression support

3. **Search History**:
   - Remember recent searches
   - Quick access to previous searches
   - Clear search history

**Proposed API**:
```java
public class SearchService {
  public List<Post> search(SearchCriteria criteria) { }
  public List<Post> searchTitle(String query) { }
  public List<Post> searchBody(String query) { }
  public List<Post> searchAuthor(String author) { }
  public List<Post> searchByDateRange(Instant start, Instant end) { }
  public List<String> getRecentSearches() { }
}

public class SearchCriteria {
  private String query;
  private SearchField field; // TITLE, BODY, AUTHOR, ALL
  private BooleanOperator operator; // AND, OR, NOT
  private Instant dateStart;
  private Instant dateEnd;
  // ... builders
}
```

**Integration Points**:
- `PostController.java`: Add `searchAdvanced(SearchCriteria)` method
- `TopBar.java`: Add "Advanced Search" button/dropdown
- New dialog: `AdvancedSearchDialog.java`

**Acceptance Criteria**:
- [ ] Field-specific search functional
- [ ] Boolean operators work correctly
- [ ] Phrase matching implemented
- [ ] Search history persists across sessions
- [ ] Advanced search dialog integrated

---

### 11. Additional FilterStrategy Implementations
**Priority**: 🟡 **MEDIUM**
**Effort**: Medium (3-4 hours)
**Status**: Not Started

**Description**:
Create additional filter strategies for richer content discovery.

**Location**: `src/main/java/org/campusboard/sgs/filter/`

---

#### 11a. DateRangeFilter.java
```java
public class DateRangeFilter implements FilterStrategy {
  private final Instant start;
  private final Instant end;

  public DateRangeFilter(Instant start, Instant end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream()
      .filter(p -> !p.createdAt().isBefore(start) && !p.createdAt().isAfter(end));
  }

  @Override
  public String getDescription() {
    return "Date Range: " + formatDate(start) + " - " + formatDate(end);
  }
}
```

**UI Integration**:
- Add "Filter by Date" button to SidebarPanel
- Date picker dialog for start/end selection

---

#### 11b. AuthorFilter.java
```java
public class AuthorFilter implements FilterStrategy {
  private final String author;

  public AuthorFilter(String author) {
    this.author = author;
  }

  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream().filter(p -> p.author().equals(author));
  }

  @Override
  public String getDescription() {
    return "Posts by @" + author;
  }
}
```

**UI Integration**:
- Click on author name in PostCard to filter by that author
- "View all posts by this author" context menu

---

#### 11c. PopularityFilter.java
```java
public class PopularityFilter implements FilterStrategy {
  private final int minLikes;

  public PopularityFilter(int minLikes) {
    this.minLikes = minLikes;
  }

  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream().filter(p -> p.likeCount() >= minLikes);
  }

  @Override
  public String getDescription() {
    return "Popular (" + minLikes + "+ likes)";
  }
}
```

**UI Integration**:
- Add "Popular Posts" button to SidebarPanel
- Slider to adjust minimum likes threshold

---

**Acceptance Criteria**:
- [ ] DateRangeFilter implemented and tested
- [ ] AuthorFilter implemented and tested
- [ ] PopularityFilter implemented and tested
- [ ] SidebarPanel updated with new filters
- [ ] Unit tests for all filters

---

### 12. User Registration System
**Priority**: 🟡 **MEDIUM**
**Effort**: Large (6-8 hours)
**Status**: Not Started

**Description**:
Add user registration capability so users can create new accounts.

**Features**:
1. **Registration Dialog**:
   - Username field (validation: 3-20 chars, alphanumeric)
   - Password field (validation: 8+ chars, complexity requirements)
   - Confirm password field
   - Email field (optional)
   - Role selection (STUDENT, STAFF) - ADMIN must be created manually

2. **Validation**:
   - Username uniqueness check
   - Password strength indicator
   - Email format validation
   - Terms of service acceptance

3. **Backend**:
   - `UserRepository.userExists(String username)` method
   - `UserRepository.add(User)` already exists
   - Password hashing (current: plain text, upgrade to BCrypt)

**Files to Create**:
- `src/main/java/org/campusboard/sgs/view/dialogs/RegisterDialog.java`

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/TopBar.java`
  - Add "Register" button next to "Login"
- `src/main/java/org/campusboard/sgs/repo/UserRepository.java`
  - Add `boolean userExists(String username)`
- `src/main/java/org/campusboard/sgs/repo/InMemoryUserRepository.java`
  - Implement `userExists()`

**Acceptance Criteria**:
- [ ] Registration dialog functional
- [ ] Username uniqueness enforced
- [ ] Password complexity validated
- [ ] New users can login after registration
- [ ] Proper error messages for validation failures

---

### 13. Password Hashing & Security
**Priority**: 🔴 **CRITICAL** (Security)
**Effort**: Medium (2-3 hours)
**Status**: Not Started

**Description**:
Replace plain-text passwords with proper hashing using BCrypt.

**Current State**:
- Passwords stored in plain text
- `AuthController.login()` does direct string comparison

**Target State**:
- Passwords hashed with BCrypt on registration/creation
- `AuthController.login()` uses BCrypt.checkpw() for validation

**Dependencies**:
Add to `build.gradle`:
```groovy
dependencies {
  implementation 'org.mindrot:jbcrypt:0.4'
  // ... existing dependencies
}
```

**Files to Modify**:
1. **InMemoryUserRepository.java**:
   ```java
   import org.mindrot.jbcrypt.BCrypt;

   @Override
   public void add(User user) {
     // Hash password before storing
     String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
     User hashedUser = new User(user.username(), hashedPassword, user.role());
     users.put(user.username(), hashedUser);
   }
   ```

2. **AuthController.java**:
   ```java
   import org.mindrot.jbcrypt.BCrypt;

   public boolean login(String username, String password) {
     var user = users.find(username).orElse(null);
     if (user != null && BCrypt.checkpw(password, user.password())) {
       session.setUser(user);
       bus.publish(Events.USER_LOGGED_IN, user.username());
       return true;
     }
     return false;
   }
   ```

3. **Main.java** seed users:
   ```java
   private static void seedUsers(InMemoryUserRepository users) {
     // Passwords will be hashed by repository
     users.add(new User("guest", "guest123", Role.GUEST));
     users.add(new User("student", "student123", Role.STUDENT));
     users.add(new User("staff", "staff123", Role.STAFF));
     users.add(new User("admin", "admin123", Role.ADMIN));
   }
   ```

**Acceptance Criteria**:
- [ ] BCrypt library added to dependencies
- [ ] Passwords hashed on user creation
- [ ] Login validates against hashed passwords
- [ ] Seed users work with new system
- [ ] No plaintext passwords in memory

---

## 🎨 LOW PRIORITY - Polish & Features (6 Items)
**Priority**: LOW
**Target**: January 2026
**Owner**: Feature Team

---

### 14. Event Calendar Integration
**Priority**: 🟢 **LOW**
**Effort**: Large (8-10 hours)
**Status**: Not Started

**Description**:
Add calendar view for Category.EVENTS posts with date/time handling.

**Features**:
1. **Calendar View**:
   - Month/week/day views
   - Event markers on dates
   - Click date to see events
   - Visual indicators for event categories

2. **Enhanced Event Posts**:
   - Date/time fields (currently just text in body)
   - Location field
   - RSVP capability
   - Event reminders

3. **Integration**:
   - New tab in MainWindow for calendar view
   - Filter feed by calendar date range
   - Export events to .ics format

**Files to Create**:
- `src/main/java/org/campusboard/sgs/view/CalendarPanel.java`
- `src/main/java/org/campusboard/sgs/model/Event.java` (extends Post)
- `src/main/java/org/campusboard/sgs/view/dialogs/CreateEventDialog.java`

**Libraries**:
- JCalendar for date picker: `com.toedter:jcalendar:1.4`

**Acceptance Criteria**:
- [ ] Calendar view displays events
- [ ] Events have proper date/time fields
- [ ] RSVP functionality works
- [ ] Export to .ics format

---

### 15. Club/Organization Profiles
**Priority**: 🟢 **LOW**
**Effort**: Large (8-10 hours)
**Status**: Not Started

**Description**:
Enhanced user model and profiles for clubs and organizations.

**Features**:
1. **Organization User Type**:
   - New UserType: ORGANIZATION
   - Multiple members can post on behalf of organization
   - Organization admins manage membership

2. **Organization Profile**:
   - Organization name
   - Description
   - Logo/banner
   - Member list
   - Contact information
   - Social media links

3. **Organization Directory**:
   - Browse all organizations
   - Filter by category (Academic, Social, Professional, etc.)
   - Follow/subscribe to organizations

**Files to Create**:
- `src/main/java/org/campusboard/sgs/model/Organization.java`
- `src/main/java/org/campusboard/sgs/view/OrganizationProfilePanel.java`
- `src/main/java/org/campusboard/sgs/view/OrganizationDirectoryPanel.java`
- `src/main/java/org/campusboard/sgs/repo/OrganizationRepository.java`

**Files to Modify**:
- `UserType.java`: Add ORGANIZATION enum value
- `User.java`: Add organization reference
- `Post.java`: Add organization attribution

**Acceptance Criteria**:
- [ ] Organizations can create accounts
- [ ] Organization profiles display correctly
- [ ] Multiple members can post for organization
- [ ] Organization directory functional

---

### 16. PostValidator.java
**Priority**: 🟢 **LOW**
**Effort**: Medium (3-4 hours)
**Status**: Not Started

**Description**:
Comprehensive validation system for post content.

**Location**: `src/main/java/org/campusboard/sgs/validation/PostValidator.java`

**Features**:
1. **Length Validation**:
   - Title: 1-100 characters
   - Body: 1-1000 characters
   - Configurable limits

2. **Content Moderation**:
   - Profanity filter
   - Spam detection (repeated content)
   - Link validation
   - HTML/script injection prevention

3. **Business Rules**:
   - Minimum time between posts (rate limiting)
   - Category-specific rules (e.g., Events must have date)
   - Role-specific limits

**Proposed API**:
```java
public class PostValidator {
  public ValidationResult validate(Post post, User author) {
    // Returns ValidationResult with errors/warnings
  }

  public boolean isValidTitle(String title) { }
  public boolean isValidBody(String body) { }
  public boolean containsProfanity(String text) { }
  public boolean isSpam(Post post, List<Post> recentPosts) { }
}

public class ValidationResult {
  private List<String> errors;
  private List<String> warnings;
  private boolean valid;
}
```

**Integration**:
- `PostController.create()`: Validate before saving
- `CreatePostDialog`: Show validation errors
- `EditPostCommand`: Validate before applying changes

**Acceptance Criteria**:
- [ ] Length validation works
- [ ] Profanity filter functional
- [ ] Spam detection prevents abuse
- [ ] Proper error messages displayed

---

### 17. Enhanced Icons and Styling
**Priority**: 🟢 **LOW**
**Effort**: Medium (3-4 hours)
**Status**: Not Started

**Description**:
Professional icon set and enhanced visual styling.

**Features**:
1. **Material Design Icons**:
   - Consistent icon set across app
   - Multiple sizes (16px, 24px, 32px)
   - Light/dark theme support

2. **Color-Coded Category Badges**:
   - Announcements: Red (#FF5252)
   - Study Groups: Blue (#448AFF)
   - Events: Green (#69F0AE)
   - Lost & Found: Orange (#FFD740)

3. **User Type Indicators**:
   - Student: Blue badge
   - Staff: Green badge
   - Admin: Red badge
   - Organization: Purple badge

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/PostCard.java`
  - Add color-coded category badges
  - Add user type indicators
- `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`
  - Add icons to filter buttons

**Resources**:
- Material Icons: https://fonts.google.com/icons
- SVG-Salamander for SVG rendering: `com.formdev:svgSalamander:1.1.4`

**Acceptance Criteria**:
- [ ] All icons replaced with Material Design
- [ ] Category badges color-coded
- [ ] User type indicators visible
- [ ] Consistent visual style

---

### 18. Responsive Design
**Priority**: 🟢 **LOW**
**Effort**: Medium (4-5 hours)
**Status**: Not Started

**Description**:
Add window resizing support and responsive layouts.

**Features**:
1. **Minimum/Maximum Sizes**:
   - Minimum: 800x600
   - Maximum: No limit
   - Remember last window size

2. **Responsive Layouts**:
   - Sidebar collapses below 1000px width
   - Feed adjusts column count based on width
   - Font sizes scale with window size

3. **Layout Adjustments**:
   - Narrow mode: Single column, no sidebar
   - Medium mode: Single column, collapsible sidebar
   - Wide mode: Multi-column feed

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/MainWindow.java`
  - Add ComponentListener for resize events
  - Implement layout switching logic
- `src/main/java/org/campusboard/sgs/view/FeedPanel.java`
  - Support multi-column layout
- `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`
  - Add collapse/expand button

**Acceptance Criteria**:
- [ ] Window can resize smoothly
- [ ] Minimum size enforced
- [ ] Layout adapts to window size
- [ ] No UI elements cut off or overlapping

---

### 19. Data Persistence & Backup
**Priority**: 🟢 **LOW**
**Effort**: Large (6-8 hours)
**Status**: Not Started

**Description**:
Add file-based persistence for posts and users.

**Features**:
1. **File Storage**:
   - Save posts to JSON file on shutdown
   - Save users to JSON file
   - Load data on startup

2. **Backup System**:
   - Automatic backups every hour
   - Keep last 10 backups
   - Restore from backup

3. **Import/Export**:
   - Export all data to JSON
   - Import data from JSON
   - CSV export for posts

**Dependencies**:
```groovy
dependencies {
  implementation 'com.google.code.gson:gson:2.10.1'
}
```

**Files to Create**:
- `src/main/java/org/campusboard/sgs/persistence/JsonPersistence.java`
- `src/main/java/org/campusboard/sgs/persistence/BackupManager.java`

**Files to Modify**:
- `Main.java`: Load data on startup, save on shutdown
- Add WindowListener for shutdown hook

**Acceptance Criteria**:
- [ ] Data persists between sessions
- [ ] Backups created automatically
- [ ] Import/export functional
- [ ] No data loss on crashes

---

## 📚 Documentation Tasks (4 Items)
**Priority**: MEDIUM
**Target**: December 2025

---

### 20. UML Class Diagrams
**Priority**: 🟡 **MEDIUM**
**Effort**: Medium (3-4 hours)
**Status**: Not Started

**Description**:
Create comprehensive UML class diagrams showing architecture.

**Diagrams Needed**:
1. **Overall System Architecture**
2. **Model Package**: Post, User, Category, UserType, Role
3. **View Package**: All view components and relationships
4. **Controller Package**: Controllers, Commands, UndoManager
5. **Repository Package**: Repository interfaces and implementations
6. **Filter Package**: FilterStrategy and implementations

**Tools**:
- PlantUML for code-based diagrams
- Lucidchart for visual editing
- Draw.io (free)

**Deliverable**: `docs/uml/`

---

### 21. Sequence Diagrams
**Priority**: 🟡 **MEDIUM**
**Effort**: Medium (3-4 hours)
**Status**: Not Started

**Description**:
Create sequence diagrams for main use cases.

**Diagrams Needed**:
1. User login sequence
2. Create post sequence (with Command pattern)
3. Like post sequence (with event propagation)
4. Filter posts sequence (with Strategy pattern)
5. Undo/redo sequence

**Deliverable**: `docs/uml/sequences/`

---

### 22. State Diagrams
**Priority**: 🟡 **MEDIUM**
**Effort**: Small (1-2 hours)
**Status**: Not Started

**Description**:
Create state diagrams for stateful objects.

**Diagrams Needed**:
1. Post lifecycle: Draft → Published → Liked → Edited → Deleted
2. User session: Logged Out → Logged In → Active → Logged Out
3. Filter state: None → Category → Search → Combined

**Deliverable**: `docs/uml/states/`

---

### 23. JavaDoc Documentation
**Priority**: 🟡 **MEDIUM**
**Effort**: Large (6-8 hours)
**Status**: Partially Complete

**Description**:
Add comprehensive JavaDoc to all public methods.

**Requirements**:
- Class-level documentation (purpose, usage)
- Method-level documentation (parameters, returns, throws)
- Package-level documentation (package-info.java)
- Examples for complex classes

**Coverage Target**: 100% of public API

**Command**:
```bash
gradle javadoc
```

**Deliverable**: `build/docs/javadoc/`

---

## 📊 Summary Statistics

### Priority Breakdown
- 🔴 **CRITICAL**: 4 items (security, permissions)
- 🟡 **MEDIUM**: 9 items (enhancements, documentation)
- 🟢 **LOW**: 7 items (polish, advanced features)

### Effort Estimates
- **Small** (< 2 hours): 4 items
- **Medium** (2-5 hours): 10 items
- **Large** (> 5 hours): 6 items
- **Total Estimated Effort**: 80-100 hours

### Category Breakdown
- **UI/UX**: 7 items (theming, icons, responsive design)
- **Features**: 6 items (search, filters, calendar, organizations)
- **Security**: 2 items (password hashing, permissions)
- **Documentation**: 4 items (UML, JavaDoc)

### Completion Timeline
- **Week 1** (Nov 12-19): URGENT items (9 items)
- **Week 2-3** (Nov 20-Dec 3): MEDIUM priority (9 items)
- **Week 4-6** (Dec 4-Dec 31): LOW priority (7 items)
- **Ongoing**: Documentation as features complete

---

## 🎯 Recommended Implementation Order

### Phase 1: Security & Permissions (Week 1)
1. Admin-only post deletion
2. Staff permission alignment
3. Verify admin menu visibility
4. Password hashing & security

### Phase 2: Core Enhancements (Week 2-3)
5. Dislike button implementation
6. User registration system
7. SearchService.java
8. Additional FilterStrategy implementations

### Phase 3: Polish (Week 4)
9. Role-based UI theming
10. Icon assets replacement
11. Enhanced icons and styling
12. Circular logo badge

### Phase 4: Advanced Features (Week 5-6)
13. Event calendar integration
14. Club/organization profiles
15. PostValidator.java
16. Responsive design
17. Data persistence & backup

### Phase 5: Documentation (Ongoing)
18. UML class diagrams
19. Sequence diagrams
20. State diagrams
21. JavaDoc documentation

---

**Document Prepared**: November 12, 2025
**Next Review**: November 19, 2025
**Status**: Ready for implementation sprint planning
