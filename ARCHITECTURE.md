# Social Group Simulator - Simplified Architecture

## ✅ Current Architecture (Clean & Working)

### Package Structure
```
org.campusboard.sgs/
├── Main.java                      # Application entry point
├── controller/
│   ├── AuthController.java        # Handles login/logout
│   └── PostController.java        # Handles posts, filter, search, likes
├── model/
│   ├── Role.java                  # GUEST, STUDENT, STAFF, ADMIN
│   ├── User.java                  # Simple: username, password, role
│   ├── Category.java              # ANNOUNCEMENTS, STUDY_GROUPS, EVENTS, LOST_FOUND
│   └── Post.java                  # Lightweight with toggleLike
├── repo/
│   ├── PostRepository.java        # Interface
│   ├── InMemoryPostRepository.java
│   ├── UserRepository.java        # Interface
│   └── InMemoryUserRepository.java
├── util/
│   ├── Session.java               # Current user session
│   ├── Events.java                # Event enum
│   └── EventBus.java              # Simple pub/sub
└── view/
    ├── TopBar.java                # Search + Login button
    ├── SideBar.java               # Category filters
    ├── FeedPanel.java             # Scrollable post feed
    └── PostCard.java              # Individual post UI
```

### Key Design Decisions

1. **No Command Pattern**: Removed UndoManager, Command interface - not needed for MVP
2. **Simple Models**: Post uses String author (username), not User object
3. **Clean Events**: util.Events enum + util.EventBus (not controller.AppEvent)
4. **Minimal Categories**: Only 4 categories needed for MVP
5. **Per-User Likes**: Post.toggleLike(String userId) uses Set<String>

### Category Enum
```java
public enum Category {
    ANNOUNCEMENTS,
    STUDY_GROUPS,
    EVENTS,
    LOST_FOUND
}
```

### Post Model
```java
public final class Post {
    private final UUID id;
    private String title, body;
    private Category category;
    private final String author;  // username string, not User object
    private final Set<String> likedBy = new HashSet<>();
    
    public Post(UUID id, String title, String body, Category cat, String author) { ... }
    public boolean toggleLike(String userId) { ... }
}
```

### Controllers
```java
// PostController - handles posts, likes, filter, search
public class PostController {
    PostController(PostRepository posts, Session session, EventBus bus)
    List<Post> current()
    void setFilter(Category c)
    void setSearch(String s)
    void create(String title, String body, Category cat)
    void toggleLike(Post p)
    void delete(UUID id)
}

// AuthController - handles login/logout
public class AuthController {
    AuthController(UserRepository users, Session session, EventBus bus)
    boolean login(String user, String pass)
    void logout()
}
```

### Demo Credentials
- `admin` / `admin123` (ADMIN)
- `staff01` / `staff123` (STAFF)
- `stud01` / `stud123` (STUDENT)

## 🔥 Bug Fixes Implemented

1. ✅ **Lost & Found in sidebar** - Category.LOST_FOUND added
2. ✅ **Guest login prompt** - PostController checks session before like
3. ✅ **Per-user likes** - Set<String> prevents duplicate likes
4. ✅ **No jump to top** - FeedPanel debounce + scroll restoration
5. ✅ **Text wrapping** - HTML in JLabel
6. ✅ **Collapsible sidebar** - JSplitPane.setOneTouchExpandable(true)

## 🚀 Build & Run

```bash
# Pull latest changes
git fetch origin
git checkout claude/mvp-finish-implementation-011CV1cGPhfMdj8yDojMNsYF
git pull origin claude/mvp-finish-implementation-011CV1cGPhfMdj8yDojMNsYF

# Clean build
./gradlew clean build

# Run application
./gradlew run

# Run tests
./gradlew test
```

## ❌ Removed (Old Architecture)

These files/packages were removed in commit `6fb22f8`:
- `Persistence/` package (now `repo/`)
- `controller/Controller.java` (split into Auth + Post controllers)
- `controller/AppEvent.java` (now `util/Events.java`)
- `controller/EventBus.java` (now `util/EventBus.java`)
- `controller/Command.java`, `UndoManager.java` (not needed)
- `controller/*Command.java` (CreatePost, DeletePost, etc.)
- `filter/` package (FilterStrategy, AllFilter, etc.)
- `model/UserType.java` (now `Role.java`)
- Complex User model with hashing/sessions
- View dialogs (LoginDialog, CreatePostDialog)

## 🎯 Tests

3 focused tests verify core functionality:
- `PostTest.java` - Like toggle mechanics
- `AuthTest.java` - Login success/failure
- `FilterTest.java` - Category filtering

## 📊 Commit History

- `6fb22f8` - Refactor to simplified architecture (latest)
- `d4086aa` - Previous MVP attempt (superseded)

Current branch is **clean** with all changes committed and pushed.
