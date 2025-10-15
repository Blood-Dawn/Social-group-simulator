# Campus Board - TODO Framework

> **Roadmap Note**: For milestone status, owners, and scheduling, refer to [docs/Roadmap.md](docs/Roadmap.md). This file tracks the actionable engineering work items linked from the roadmap.

## 🎯 **Application Overview**
A campus social platform where students, staff, clubs, and organizations can post announcements, events, discussions, and more. Think of it as a digital bulletin board for college campuses.

## � **Demo Login Credentials**
Keep these handy for local testing with the in-memory repositories:

| Username | Password  | Role                |
|----------|-----------|--------------------|
| `admin`  | `admin123`  | Staff/Admin controls |
| `staff`  | `staff123`  | Staff              |
| `student`| `student123`| Student            |
| `guest`  | `guest123`  | Guest              |

> If remote mode is enabled, align the backing service credentials with this table.

## 🧰 **Get It Done – Post-Launch Troubleshooting**
Rapid follow-ups from the latest run-through. Assign and knock these out before the next demo:

- [ ] Restrict post deletion to admin-level accounts; other roles should not see the delete affordance.
- [ ] Align staff capabilities with students (no elevated privileges beyond current student feature set).
- [ ] Only admin users should see admin tools in the sidebar panel; hide these controls for all other roles.
- [ ] Introduce unique UI themes or layouts per user role for clearer differentiation.
- [ ] Preserve the feed scroll position when a post is liked so the view does not jump to the top.
- [ ] Enforce single-like interactions: first tap likes, second tap removes the like, and block duplicate reactions.
- [ ] Add a companion dislike button with proper toggling behavior and corresponding event wiring.
- [ ] Replace placeholder filter and sidebar icons with finalized artwork assets.
- [ ] Update the top-left "F" logo to render inside a circular badge instead of the current square for a sleeker look.

## �🚧 **High Priority - Core Functionality**

### Data Layer (Repository Implementations)
> *Roadmap Alignment*: Milestone **Authentication Foundation**
- [ ] **TODO: Complete InMemoryUserRepository.java**
  - Implement all methods using the `users` ConcurrentHashMap
  - Add proper validation and error handling
  - Location: Already created, needs implementation

- [ ] **TODO: Add missing fields to Post.java**
  - Add `User author` field (replace String author)
  - Update constructors to require User parameter
  - Location: `src/main/java/org/campusboard/sgs/model/Post.java`

### Controller Layer (Business Logic)
> *Roadmap Alignment*: Milestone **Authentication Foundation**
- [ ] **TODO: Complete Controller.java methods**
  - All methods have signatures and TODO comments
  - Implement validation, repository calls, event publishing
  - Location: `src/main/java/org/campusboard/sgs/controller/Controller.java`

### View Layer (UI Implementation) - ✅ **COMPLETED BY DEO (10/14/2025)**
> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

- [x] **✅ DONE: Complete MainWindow.java**
  - ✅ Layout implementation complete with BorderLayout
  - ✅ Component wiring and event handling implemented
  - ✅ Thread-safe UI updates with SwingUtilities.invokeLater
  - ✅ EventBus integration ready (waiting on backend update)
  - Location: `src/main/java/org/campusboard/sgs/view/MainWindow.java`

- [x] **✅ DONE: Complete TopBar.java**
  - ✅ Search functionality with real-time filtering implemented
  - ✅ Create post button with dialog integration
  - ✅ Login/logout UI controls ready (waiting on Controller methods)
  - ✅ FAU branding and professional styling
  - ✅ Hover effects and modern design patterns
  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`

- [x] **✅ DONE: Complete FeedPanel.java**
  - ✅ Scrollable posts display with BoxLayout
  - ✅ Post filtering by category implemented
  - ✅ Search filtering implemented
  - ✅ Dual filtering system (category + search)
  - ✅ Empty state handling
  - ✅ EventBus subscriptions ready (waiting on backend update)
  - Location: `src/main/java/org/campusboard/sgs/view/FeedPanel.java`

- [x] **✅ DONE: Complete PostCard.java**
  - ✅ Professional post display with all fields
  - ✅ Like/dislike button functionality implemented
  - ✅ Custom painted circular avatars
  - ✅ Human-readable timestamp formatting
  - ✅ Category badges with styling
  - ✅ Delete button with confirmation
  - ✅ Hover effects and shadows
  - Location: `src/main/java/org/campusboard/sgs/view/PostCard.java`

- [x] **✅ DONE: Complete SidebarPanel.java**
  - ✅ Category filtering with blue selection system
  - ✅ Quick access shortcuts
  - ✅ All category buttons with icons
  - ✅ Clear filters button
  - ✅ Hover effects
  - ✅ EventBus integration ready (waiting on backend update)
  - Location: `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`

- [x] **✅ DONE: Complete CreatePostDialog.java**
  - ✅ Form validation (title, body, category)
  - ✅ Character limits and proper styling
  - ✅ Keyboard shortcuts (Enter to advance fields)
  - ✅ Error dialogs for validation
  - ✅ Category dropdown with all options
  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/CreatePostDialog.java`

### Main Application Wiring - ✅ **COMPLETED BY Kheiven (10/15/2025)**
> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**
- [x] **TODO: Complete Main.java**
  - ✅Wire up all components with dependency injection
  - ✅ Initialize repositories and controllers
  - ✅ Location: `src/main/java/org/campusboard/sgs/Main.java`

## 🔧 **Medium Priority - Enhancements**

### Authentication System
> *Roadmap Alignment*: Milestone **Authentication Foundation**
- [ ] **TODO: Create LoginDialog.java**
  - Username/password form
  - Integration with UserRepository
  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/LoginDialog.java`

### Search & Filter Enhancements
> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience** and feeds into **Real-time Sync & Notifications**
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
> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**
- [ ] **TODO: Create Post Commands**
  - `CreatePostCommand.java` - implements Command interface
  - `DeletePostCommand.java` - implements Command interface
  - Wire up with UndoManager in Controller
  - Location: `src/main/java/org/campusboard/sgs/controller/commands/`

## 🎨 **Low Priority - Polish & Features**

### Campus-Specific Features
> *Roadmap Alignment*: Milestone **Real-time Sync & Notifications**
- [ ] **TODO: Event Calendar Integration**
  - Special handling for Category.EVENTS posts
  - Calendar view component
  
- [ ] **TODO: Club/Organization Profiles**
  - Enhanced User model for organizations
  - Club-specific posting privileges

### Validation & Error Handling
> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**
- [ ] **TODO: Create PostValidator.java**
  - Title length (1-200 characters)
  - Body length (1-5000 characters)
  - Content moderation rules
  - Location: `src/main/java/org/campusboard/sgs/validation/`

### UI Polish
> *Roadmap Alignment*: Milestone **Real-time Sync & Notifications**
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
