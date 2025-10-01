# Campus Board - TODO Framework

## 🎯 **Application Overview**
A campus social platform where students, staff, clubs, and organizations can post announcements, events, discussions, and more. Think of it as a digital bulletin board for college campuses.

## 🚧 **High Priority - Core Functionality**

### Data Layer (Repository Implementations)
- [ ] **TODO: Complete InMemoryUserRepository.java**
  - Implement all methods using the `users` ConcurrentHashMap
  - Add proper validation and error handling
  - Location: Already created, needs implementation

- [ ] **TODO: Add missing fields to Post.java**
  - Add `User author` field (replace String author)
  - Update constructors to require User parameter
  - Location: `src/main/java/org/campusboard/sgs/model/Post.java`

### Controller Layer (Business Logic)
- [ ] **TODO: Complete Controller.java methods**
  - All methods have signatures and TODO comments
  - Implement validation, repository calls, event publishing
  - Location: `src/main/java/org/campusboard/sgs/controller/Controller.java`

### View Layer (UI Implementation)
- [ ] **TODO: Complete MainWindow.java**
  - Implement all TODO methods for layout and event handling
  - Wire up components properly
  - Location: `src/main/java/org/campusboard/sgs/view/MainWindow.java`

- [ ] **TODO: Complete TopBar.java**
  - Implement search functionality
  - Add create post button action
  - Add login/logout functionality
  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`

- [ ] **TODO: Complete FeedPanel.java**
  - Implement scrollable posts display
  - Handle post filtering and searching
  - Location: `src/main/java/org/campusboard/sgs/view/FeedPanel.java`

- [ ] **TODO: Complete PostCard.java**
  - Implement post display with like/dislike buttons
  - Add proper styling and layout
  - Location: `src/main/java/org/campusboard/sgs/view/PostCard.java`

- [ ] **TODO: Complete SidebarPanel.java**
  - Implement category filtering
  - Add campus-specific quick actions
  - Location: `src/main/java/org/campusboard/sgs/view/SidbarPanel.java`

- [ ] **TODO: Complete CreatePostDialog.java**
  - Implement form validation
  - Add character limits and proper styling
  - Location: Already created, needs implementation

### Main Application Wiring
- [ ] **TODO: Complete Main.java**
  - Wire up all components with dependency injection
  - Initialize repositories and controllers
  - Location: `src/main/java/org/campusboard/sgs/Main.java`

## 🔧 **Medium Priority - Enhancements**

### Authentication System
- [ ] **TODO: Create LoginDialog.java**
  - Username/password form
  - Integration with UserRepository
  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/LoginDialog.java`

### Search & Filter Enhancements
- [ ] **TODO: Create SearchService.java**
  - Advanced search functionality
  - Search by title, body, author, category
  - Location: `src/main/java/org/campusboard/sgs/service/SearchService.java`

- [ ] **TODO: Add more FilterStrategy implementations**
  - `DateRangeFilter.java` - filter by creation date
  - `AuthorFilter.java` - filter by post author  
  - `PopularityFilter.java` - filter by likes/engagement
  - Location: `src/main/java/org/campusboard/sgs/filter/`

### Command Pattern for Undo/Redo
- [ ] **TODO: Create Post Commands**
  - `CreatePostCommand.java` - implements Command interface
  - `DeletePostCommand.java` - implements Command interface
  - Wire up with UndoManager in Controller
  - Location: `src/main/java/org/campusboard/sgs/controller/commands/`

## 🎨 **Low Priority - Polish & Features**

### Campus-Specific Features
- [ ] **TODO: Event Calendar Integration**
  - Special handling for Category.EVENTS posts
  - Calendar view component
  
- [ ] **TODO: Club/Organization Profiles**
  - Enhanced User model for organizations
  - Club-specific posting privileges

### Validation & Error Handling
- [ ] **TODO: Create PostValidator.java**
  - Title length (1-200 characters)
  - Body length (1-5000 characters)
  - Content moderation rules
  - Location: `src/main/java/org/campusboard/sgs/validation/`

### UI Polish
- [ ] **TODO: Add Icons and Styling**
  - Like/dislike icons
  - Category badges with colors
  - User type indicators
  
- [ ] **TODO: Responsive Design**
  - Window resizing support
  - Mobile-friendly layouts

## 📋 **Implementation Guidelines**

### Code Structure
- **Framework Owner** (You): Creates class structures, method signatures, TODOs
- **Implementation Team**: Fills in method bodies following TODO comments
- All UI components should extend appropriate Swing classes
- Use dependency injection pattern throughout

### Event System
- Publish events for all user actions using EventBus
- Subscribe to events in view components for UI updates
- Follow existing AppEvent enum patterns

### Campus Board Specific Requirements
- Support multiple user types (students, staff, clubs, etc.)
- Category-based organization for campus content
- Search and filter capabilities for large amounts of posts
- Simple but effective UI suitable for daily campus use

### Data Flow
```
User Action → View → Controller → Repository → Model
                ↓
            EventBus → Update Other Views
```

## ✅ **Files Created & Ready for Implementation**
- ✅ User.java (model structure complete)
- ✅ UserType.java (enum complete)
- ✅ UserRepository.java (interface complete)  
- ✅ InMemoryUserRepository.java (needs method implementation)
- ✅ Controller.java (methods signatures + TODOs)
- ✅ All View classes (structure + TODOs)
- ✅ CreatePostDialog.java (structure + TODOs)
- ✅ Main.java (wiring TODOs)

**Total TODO Items**: ~35 implementation tasks
**Estimated Time**: 2-3 weeks for full implementation team