# Campus Board - TODO Framework

## 🎯 **Application Overview**

> **Roadmap Note**: For milestone status, owners, and scheduling, refer to [docs/Roadmap.md](docs/Roadmap.md). This file tracks the actionable engineering work items linked from the roadmap.

A campus social platform where students, staff, clubs, and organizations can post announcements, events, discussions, and more. Think of it as a digital bulletin board for college campuses.

---

## 🔐 **Demo Login Credentials** - **ADDED BY kheiven (10/15/2025)**

Keep these handy for local testing with the in-memory repositories:

| Username | Password    | Role                 |
|----------|-------------|----------------------|
| `admin`  | `admin123`  | Staff/Admin controls |
| `staff`  | `staff123`  | Staff                |
| `student`| `student123`| Student              |
| `guest`  | `guest123`  | Guest                |

> If remote mode is enabled, align the backing service credentials with this table.

---

## 🚨 **URGENT - Get It Done (Post-Launch Troubleshooting)** - **ADDED BY kheiven (10/15/2025)**

**Priority**: Rapid follow-ups from the latest run-through. Assign and knock these out before the next demo.

- [ ] **Admin-Only Post Deletion** - Restrict post deletion to admin-level accounts; other roles should not see the delete affordance.
- [ ] **Staff Permission Alignment** - Align staff capabilities with students (no elevated privileges beyond current student feature set).
- [ ] **Admin-Only Sidebar Tools** - Only admin users should see admin tools in the sidebar panel; hide these controls for all other roles.
- [ ] **Role-Based UI Theming** - Introduce unique UI themes or layouts per user role for clearer differentiation.
- [ ] **Preserve Scroll Position on Like** - Preserve the feed scroll position when a post is liked so the view does not jump to the top.
- [ ] **Single-Like Enforcement** - Enforce single-like interactions: first tap likes, second tap removes the like, and block duplicate reactions.
- [ ] **Dislike Button Implementation** - Add a companion dislike button with proper toggling behavior and corresponding event wiring.
- [ ] **Icon Assets** - Replace placeholder filter and sidebar icons with finalized artwork assets.
- [ ] **Circular Logo Badge** - Update the top-left "F" logo to render inside a circular badge instead of the current square for a sleeker look.

---

## ✅ **High Priority - Core Functionality** - **100% COMPLETE!**

### Data Layer (Repository Implementations)

> *Roadmap Alignment*: Milestone **Authentication Foundation**

- [x] **✅ DONE: Complete InMemoryUserRepository.java** - **COMPLETED (11/11/2025)**
  - ✅ Implemented all methods using the `users` ConcurrentHashMap
  - ✅ Added proper validation and error handling
  - ✅ Implemented password hashing and validation
  - ✅ Location: `src/main/java/org/campusboard/sgs/Persistence/InMemoryUserRepository.java`

- [x] **✅ DONE: Add missing fields to Post.java** - **COMPLETED (11/11/2025)**
  - ✅ Added `User author` field (replaced String author)
  - ✅ Updated constructors to require User parameter
  - ✅ Added GUEST_AUTHOR_FALLBACK for guest posts
  - ✅ Location: `src/main/java/org/campusboard/sgs/model/Post.java`

### Controller Layer (Business Logic)

> *Roadmap Alignment*: Milestone **Authentication Foundation**

- [x] **✅ DONE: Complete Controller.java methods** - **COMPLETED (11/11/2025)**
  - ✅ All methods fully implemented
  - ✅ Validation, repository calls, event publishing complete
  - ✅ Authentication system with AuthenticationResult
  - ✅ Post management (create, delete, like, dislike)
  - ✅ User management and session handling
  - ✅ Location: `src/main/java/org/campusboard/sgs/controller/Controller.java`

### View Layer (UI Implementation) - ✅ **COMPLETED BY DEO (10/14/2025)**

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

- [x] **✅ DONE: Complete MainWindow.java**
  - ✅ Layout implementation complete with BorderLayout
  - ✅ Component wiring and event handling implemented
  - ✅ Thread-safe UI updates with SwingUtilities.invokeLater
  - ✅ EventBus integration ready
  - Location: `src/main/java/org/campusboard/sgs/view/MainWindow.java`

- [x] **✅ DONE: Complete TopBar.java**
  - ✅ Search functionality with real-time filtering implemented
  - ✅ Create post button with dialog integration
  - ✅ Login/logout UI controls complete
  - ✅ FAU branding and professional styling
  - ✅ Hover effects and modern design patterns
  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`

- [x] **✅ DONE: Complete FeedPanel.java**
  - ✅ Scrollable posts display with BoxLayout
  - ✅ Post filtering by category implemented
  - ✅ Search filtering implemented
  - ✅ Dual filtering system (category + search)
  - ✅ Empty state handling
  - ✅ EventBus subscriptions complete
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
  - ✅ EventBus integration complete
  - Location: `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`

- [x] **✅ DONE: Complete CreatePostDialog.java**
  - ✅ Form validation (title, body, category)
  - ✅ Character limits and proper styling
  - ✅ Keyboard shortcuts (Enter to advance fields)
  - ✅ Error dialogs for validation
  - ✅ Category dropdown with all options
  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/CreatePostDialog.java`

---

## 🔧 **Medium Priority - Enhancements**

### Authentication System - ✅ **COMPLETED (11/11/2025)**

> *Roadmap Alignment*: Milestone **Authentication Foundation**

- [x] **✅ DONE: Create LoginDialog.java**
  - ✅ Username/password form with validation
  - ✅ Integration with UserRepository and Controller
  - ✅ Secure password handling with array clearing
  - ✅ Error feedback display
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

### Command Pattern for Undo/Redo - ✅ **COMPLETED (11/11/2025)**

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

- [x] **✅ DONE: Create Post Commands**
  - ✅ `CreatePostCommand.java` - implements Command interface
  - ✅ `DeletePostCommand.java` - implements Command interface
  - ✅ `EditPostCommand.java` - implements Command interface
  - ✅ `LikePostCommand.java` - implements Command interface
  - ✅ All commands wired with EventBus integration
  - Location: `src/main/java/org/campusboard/sgs/controller/`

---

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

---

## 🔗 **Main Application Wiring & Backend Integration** - ✅ **COMPLETED BY kheiven (10/15/2025)**

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

- [x] **✅ DONE: Main.java Integration**
  - ✅ Added remote repository support with configuration helpers
  - ✅ Implemented shouldUseRemoteRepository(), resolveRemoteUrl(), resolvePollInterval()
  - ✅ Wire up RemotePostSyncClient for polling
  - ✅ Added WindowAdapter for clean sync client shutdown
  - ✅ Initialize repositories and controllers
  - Location: `src/main/java/org/campusboard/sgs/Main.java`

- [x] **✅ DONE: Category.java Enhancement**
  - ✅ Added CAMPUS_SERVICES enum value for demo data
  - Location: `src/main/java/org/campusboard/sgs/model/Category.java`

- [x] **✅ DONE: TopBar.java Authentication Fix**
  - ✅ Added missing icon fields (guestAvatarIcon, userAvatarIcon, loginDoorIcon, logoutDoorIcon)
  - ✅ Created userInfoPanel and initialized in createComponents()
  - ✅ Replaced simple username prompt with username/password dialog
  - ✅ Integrated Controller.AuthenticationResult for proper auth flow
  - ✅ Added secure password clearing after authentication attempts
  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`

---

## 📋 **Implementation Guidelines**

### Code Structure
- **Framework Owner** (Architect): Creates class structures, method signatures, TODOs
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

---

## 📊 **Project Stats** - **Updated 11/11/2025**

- **Total TODO Items**: ~35 implementation tasks
- **✅ Completed**: 18 items (51%)
- **🚨 Urgent Items**: 9 post-launch fixes (UI enhancements)
- **🔧 High Priority**: 0 remaining (all core functionality complete!)
- **📦 Medium Priority**: 2 enhancement items (filters, search service)
- **🎨 Low Priority**: 6 polish & feature items (icons, themes, calendar, validation)
- **Major Milestones Completed**: 
  - ✅ Authentication Foundation (100%)
  - ✅ All UI Components (100%)
  - ✅ Command Pattern Implementation (100%)
  - ✅ Repository Layer (100%)
  - ✅ Main Application Wiring (100%)
- **Next Focus**: UI polish, role-based permissions, and advanced features

---

## ✅ **Files Created & Fully Implemented**

- ✅ User.java (model structure complete with all fields)
- ✅ UserType.java (enum complete)
- ✅ UserRepository.java (interface complete)
- ✅ InMemoryUserRepository.java (fully implemented with password hashing)
- ✅ Post.java (fully implemented with User author)
- ✅ Controller.java (all methods implemented)
- ✅ All View classes (complete and functional)
- ✅ CreatePostDialog.java (complete with validation)
- ✅ LoginDialog.java (complete with authentication)
- ✅ Main.java (fully wired with dependency injection)
- ✅ CreatePostCommand.java (complete)
- ✅ DeletePostCommand.java (complete)
- ✅ EditPostCommand.java (complete)
- ✅ LikePostCommand.java (complete)
- ✅ EventBus.java (complete)
- ✅ UndoManager.java (complete)
