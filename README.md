# Campus Board - Social Group Simulator# Social Group Simulator



A digital campus bulletin board where students, staff, clubs, and organizations can share announcements, events, discussions, and more. Built for college campuses to facilitate community communication and engagement.Java Swing desktop application simulating a campus bulletin board.



## 🎯 Project Overview## Features (MVP)

- Create and view posts (events, study groups, lost & found)

Campus Board is a Java Swing-based social platform designed specifically for college environments. It provides a centralized space for:- Filter and sort posts

- Comments and reactions

- **Official Announcements** from administration and faculty- Undo/redo using Command pattern

- **Student Events** and campus activities- Observer/EventBus for updates

- **Club & Organization** posts and updates

- **Academic Discussions** and study groups## Build & Run

- **Campus Services** like housing, jobs, buy/sell```

- **Social Interactions** with likes, filtering, and search./gradlew run

```

## 🏗️ Architecture

### Framework Design Philosophy
This project uses a **Framework + Implementation** approach:
- **Framework Owner** (Architect): Creates class structures, method signatures, and TODO guidance
- **Implementation Team**: Fills in method bodies following detailed TODO comments
- **Clean Separation**: Architecture decisions vs implementation details

### Technology Stack
- **Frontend**: Java Swing (Desktop Application)
- **Architecture**: MVC Pattern with Event Bus
- **Data**: In-Memory repositories (expandable to databases)
- **Build**: Gradle

### Project Structure
src/main/java/org/campusboard/sgs/
├── Main.java                    # Application entry point
├── controller/                  # Business logic layer
│   ├── Controller.java         # Main controller with TODO methods
│   ├── EventBus.java          # Decoupled event system
│   ├── AppEvent.java          # Event types enum
│   ├── Command.java           # Command pattern interface
│   └── UndoManager.java       # Undo/redo functionality
├── model/                      # Data models
│   ├── User.java              # User entity (students, staff, clubs)
│   ├── UserType.java          # User categories enum
│   ├── Post.java              # Post entity with campus-specific fields
│   └── Category.java          # Content categories 
├── Persistence/               # Data access layer
│   ├── PostRepository.java    # Post data interface
│   ├── UserRepository.java    # User data interface
│   ├── InMemoryPostRepository.java
│   └── InMemoryUserRepository.java
├── view/                      # User interface
│   ├── MainWindow.java        # Main application window
│   ├── TopBar.java           # Search, create post, user actions
│   ├── SidebarPanel.java     # Category filtering, navigation
│   ├── FeedPanel.java        # Scrollable posts display
│   ├── PostCard.java         # Individual post component
│   └── dialogs/
│       └── CreatePostDialog.java
└── filter/                    # Content filtering
    ├── FilterStrategy.java    # Strategy pattern interface
    └── CategoryFilter.java    # Category-based filtering
```

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Gradle 7.0 or higher

### Building the Project
```bash
./gradlew build
```

### Running the Application
```bash
./gradlew run
```

Currently shows a "Coming Soon" window until implementation is complete.

## 📋 Implementation Status

### ✅ Complete (Framework)
- [x] All class structures and interfaces
- [x] Method signatures with comprehensive TODOs
- [x] Event system architecture
- [x] Repository pattern setup
- [x] UI component structure
- [x] Category and user type enums

### 🚧 In Progress (Implementation Needed)
See [TODO.md](TODO.md) for detailed implementation tasks (~35 TODO items)

#### High Priority
- [ ] Repository method implementations
- [ ] Controller business logic
- [ ] UI component implementations
- [ ] Event handling wiring

#### Medium Priority  
- [ ] Authentication system
- [ ] Search functionality
- [ ] Advanced filtering
- [ ] Command pattern for undo/redo

#### Low Priority
- [ ] UI polish and styling
- [ ] Campus-specific features
- [ ] Validation and error handling

## 🎓 Campus-Specific Features

### User Types
- **Students**: Create posts, join discussions, like/dislike
- **Faculty**: Official announcements, course-related posts
- **Staff**: Administrative updates, campus services
- **Clubs/Organizations**: Event announcements, member recruitment
- **Administration**: Campus-wide announcements
- **Alumni**: Networking, mentorship posts

### Content Categories
Organized into logical groups:
- **Campus Essentials**: Announcements, Events, Academics, Campus Life
- **Student Services**: Housing, Jobs, Buy/Sell, Lost & Found
- **Social & Activities**: Clubs, Study Groups, Sports, Volunteering
- **Academic Subjects**: STEM, Humanities, Social Sciences
- **System Generated**: Featured, Trending, Recommended content

## 🔧 Development Guidelines

### Code Standards
- Follow existing naming conventions and package structure
- All public methods require JavaDoc comments
- Use dependency injection throughout
- Implement comprehensive error handling

### Event-Driven Architecture
```java
// Example: Creating a post triggers multiple events
controller.createPost(title, body, category);
// → Publishes: POST_CREATED, POSTS_CHANGED
// → UI components auto-update via EventBus subscriptions
```

### TODO Implementation Pattern
Each class follows this pattern:
```java
public class ExampleClass {
    // Framework: Constructor, fields, method signatures
    
    public void exampleMethod() {
        // TODO: Specific implementation guidance
        // TODO: Step-by-step instructions
        // TODO: Expected behavior and edge cases
    }
}
```

## 🤝 Contributing

### For Implementation Team
1. Choose a TODO item from [TODO.md](TODO.md)
2. Follow the detailed TODO comments in the code
3. Implement method bodies without changing signatures
4. Test your implementation thoroughly
5. Update TODO.md when complete

### For Architecture Changes
- Discuss with framework owner before modifying class structures
- All architectural changes require approval
- Maintain consistency with existing patterns

## 📱 UI Preview (Planned)

```
┌─────────────────────────────────────────────────────────┐
│ [🏠 Campus Board] [🔍 Search...] [➕ Create] [👤 User] │ TopBar
├──────────┬──────────────────────────────────────────────┤
│📁 All    │ 📌 📝 Welcome Week Events                    │
│📢 Announce│   📅 Posted 2h ago by @StudentLife         │
│📅 Events │   💙 15 👎 2                               │
│🏫 Clubs  │ ─────────────────────────────────────────── │
│📚 Academics│ 🔬 Looking for Study Group - Organic Chem │
│🏠 Housing│   📅 Posted 4h ago by @ChemStudent         │
│💼 Jobs   │   💙 8 👎 0                                │
│🔍 Search │ ─────────────────────────────────────────── │
│         │ 🏀 Basketball Tryouts This Friday!         │
└─────────┴──────────────────────────────────────────────┘
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Support

For questions about implementation or architecture:
- Check [TODO.md](TODO.md) for detailed guidance
- Review existing code patterns and TODO comments
- Create an issue for clarification

---

**Built for campus communities, by campus communities** 🎓