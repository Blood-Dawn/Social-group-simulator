# Campus Board - TODO Framework# Campus Board - TODO Framework



## 🎯 **Application Overview**> **Roadmap Note**: For milestone status, owners, and scheduling, refer to [docs/Roadmap.md](docs/Roadmap.md). This file tracks the actionable engineering work items linked from the roadmap.

A campus social platform where students, staff, clubs, and organizations can post announcements, events, discussions, and more. Think of it as a digital bulletin board for college campuses.

## 🎯 **Application Overview**

---A campus social platform where students, staff, clubs, and organizations can post announcements, events, discussions, and more. Think of it as a digital bulletin board for college campuses.



## 🔐 **Demo Login Credentials** - **ADDED BY kheiven (10/15/2025)**## 🔐 **Demo Login Credentials** - **ADDED BY kheiven (10/15/2025)**

Keep these handy for local testing with the in-memory repositories:Keep these handy for local testing with the in-memory repositories:



| Username | Password    | Role                 || Username | Password  | Role                |

|----------|-------------|----------------------||----------|-----------|--------------------|

| `admin`  | `admin123`  | Staff/Admin controls || `admin`  | `admin123`  | Staff/Admin controls |

| `staff`  | `staff123`  | Staff                || `staff`  | `staff123`  | Staff              |

| `student`| `student123`| Student              || `student`| `student123`| Student            |

| `guest`  | `guest123`  | Guest                || `guest`  | `guest123`  | Guest              |



> **Note**: If remote mode is enabled, align the backing service credentials with this table.> If remote mode is enabled, align the backing service credentials with this table.



---## 🧰 **Get It Done – Post-Launch Troubleshooting** - **ADDED BY kheiven (10/15/2025)**

Rapid follow-ups from the latest run-through. Assign and knock these out before the next demo:

## 📍 **Roadmap Note**

For milestone status, owners, and scheduling, refer to [docs/Roadmap.md](docs/Roadmap.md). This file tracks the actionable engineering work items linked from the roadmap.- [ ] Restrict post deletion to admin-level accounts; other roles should not see the delete affordance.

- [ ] Align staff capabilities with students (no elevated privileges beyond current student feature set).

---- [ ] Only admin users should see admin tools in the sidebar panel; hide these controls for all other roles.

- [ ] Introduce unique UI themes or layouts per user role for clearer differentiation.

## 🚨 **URGENT - Get It Done (Post-Launch Troubleshooting)** - **ADDED BY kheiven (10/15/2025)**- [ ] Preserve the feed scroll position when a post is liked so the view does not jump to the top.

**Priority**: Rapid follow-ups from the latest run-through. Assign and knock these out before the next demo.- [ ] Enforce single-like interactions: first tap likes, second tap removes the like, and block duplicate reactions.

- [ ] Add a companion dislike button with proper toggling behavior and corresponding event wiring.

- [ ] **Admin-Only Post Deletion** - Restrict post deletion to admin-level accounts; other roles should not see the delete affordance.- [ ] Replace placeholder filter and sidebar icons with finalized artwork assets.

- [ ] **Staff Permission Alignment** - Align staff capabilities with students (no elevated privileges beyond current student feature set).- [ ] Update the top-left "F" logo to render inside a circular badge instead of the current square for a sleeker look.

- [ ] **Admin-Only Sidebar Tools** - Only admin users should see admin tools in the sidebar panel; hide these controls for all other roles.

- [ ] **Role-Based UI Theming** - Introduce unique UI themes or layouts per user role for clearer differentiation.## �🚧 **High Priority - Core Functionality**

- [ ] **Preserve Scroll Position on Like** - Preserve the feed scroll position when a post is liked so the view does not jump to the top.

- [ ] **Single-Like Enforcement** - Enforce single-like interactions: first tap likes, second tap removes the like, and block duplicate reactions.### Data Layer (Repository Implementations)

- [ ] **Dislike Button Implementation** - Add a companion dislike button with proper toggling behavior and corresponding event wiring.> *Roadmap Alignment*: Milestone **Authentication Foundation**

- [ ] **Icon Assets** - Replace placeholder filter and sidebar icons with finalized artwork assets.- [ ] **TODO: Complete InMemoryUserRepository.java**

- [ ] **Circular Logo Badge** - Update the top-left "F" logo to render inside a circular badge instead of the current square for a sleeker look.  - Implement all methods using the `users` ConcurrentHashMap

  - Add proper validation and error handling

---  - Location: Already created, needs implementation



## 🔥 **High Priority - Core Functionality**- [ ] **TODO: Add missing fields to Post.java**

  - Add `User author` field (replace String author)

### 📊 Data Layer (Repository Implementations)  - Update constructors to require User parameter

> *Roadmap Alignment*: Milestone **Authentication Foundation**  - Location: `src/main/java/org/campusboard/sgs/model/Post.java`



- [ ] **Complete InMemoryUserRepository.java**### Controller Layer (Business Logic)

  - Implement all methods using the `users` ConcurrentHashMap> *Roadmap Alignment*: Milestone **Authentication Foundation**

  - Add proper validation and error handling- [ ] **TODO: Complete Controller.java methods**

  - Location: `src/main/java/org/campusboard/sgs/Persistence/InMemoryUserRepository.java`  - All methods have signatures and TODO comments

  - Implement validation, repository calls, event publishing

- [ ] **Add Missing Fields to Post.java**  - Location: `src/main/java/org/campusboard/sgs/controller/Controller.java`

  - Add `User author` field (replace String author)

  - Update constructors to require User parameter### View Layer (UI Implementation) - ✅ **COMPLETED BY DEO (10/14/2025)**

  - Location: `src/main/java/org/campusboard/sgs/model/Post.java`> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**



### 🎮 Controller Layer (Business Logic)- [x] **✅ DONE: Complete MainWindow.java**

> *Roadmap Alignment*: Milestone **Authentication Foundation**  - ✅ Layout implementation complete with BorderLayout

  - ✅ Component wiring and event handling implemented

- [ ] **Complete Controller.java Methods**  - ✅ Thread-safe UI updates with SwingUtilities.invokeLater

  - All methods have signatures and TODO comments  - ✅ EventBus integration ready (waiting on backend update)

  - Implement validation, repository calls, event publishing  - Location: `src/main/java/org/campusboard/sgs/view/MainWindow.java`

  - Location: `src/main/java/org/campusboard/sgs/controller/Controller.java`

- [x] **✅ DONE: Complete TopBar.java**

---  - ✅ Search functionality with real-time filtering implemented

  - ✅ Create post button with dialog integration

## 🔧 **Medium Priority - Enhancements**  - ✅ Login/logout UI controls ready (waiting on Controller methods)

  - ✅ FAU branding and professional styling

### 🔐 Authentication System  - ✅ Hover effects and modern design patterns

> *Roadmap Alignment*: Milestone **Authentication Foundation**  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`



- [ ] **Create LoginDialog.java**- [x] **✅ DONE: Complete FeedPanel.java**

  - Username/password form  - ✅ Scrollable posts display with BoxLayout

  - Integration with UserRepository  - ✅ Post filtering by category implemented

  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/LoginDialog.java`  - ✅ Search filtering implemented

  - ✅ Dual filtering system (category + search)

### 🔍 Search & Filter Enhancements  - ✅ Empty state handling

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience** and feeds into **Real-time Sync & Notifications**  - ✅ EventBus subscriptions ready (waiting on backend update)

  - Location: `src/main/java/org/campusboard/sgs/view/FeedPanel.java`

- [ ] **Create SearchService.java**

  - Advanced search functionality- [x] **✅ DONE: Complete PostCard.java**

  - Search by title, body, author, category  - ✅ Professional post display with all fields

  - Location: `src/main/java/org/campusboard/sgs/service/SearchService.java`  - ✅ Like/dislike button functionality implemented

  - ✅ Custom painted circular avatars

- [ ] **Add More FilterStrategy Implementations**  - ✅ Human-readable timestamp formatting

  - `DateRangeFilter.java` - filter by creation date  - ✅ Category badges with styling

  - `AuthorFilter.java` - filter by post author  - ✅ Delete button with confirmation

  - `PopularityFilter.java` - filter by likes/engagement  - ✅ Hover effects and shadows

  - Location: `src/main/java/org/campusboard/sgs/filter/`  - Location: `src/main/java/org/campusboard/sgs/view/PostCard.java`



### ↩️ Command Pattern for Undo/Redo- [x] **✅ DONE: Complete SidebarPanel.java**

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**  - ✅ Category filtering with blue selection system

  - ✅ Quick access shortcuts

- [ ] **Create Post Commands**  - ✅ All category buttons with icons

  - `CreatePostCommand.java` - implements Command interface  - ✅ Clear filters button

  - `DeletePostCommand.java` - implements Command interface  - ✅ Hover effects

  - Wire up with UndoManager in Controller  - ✅ EventBus integration ready (waiting on backend update)

  - Location: `src/main/java/org/campusboard/sgs/controller/commands/`  - Location: `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`



---- [x] **✅ DONE: Complete CreatePostDialog.java**

  - ✅ Form validation (title, body, category)

## 🎨 **Low Priority - Polish & Features**  - ✅ Character limits and proper styling

  - ✅ Keyboard shortcuts (Enter to advance fields)

### 🎓 Campus-Specific Features  - ✅ Error dialogs for validation

> *Roadmap Alignment*: Milestone **Real-time Sync & Notifications**  - ✅ Category dropdown with all options

  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/CreatePostDialog.java`

- [ ] **Event Calendar Integration**

  - Special handling for Category.EVENTS posts### Main Application Wiring & Backend Integration - ✅ **COMPLETED BY kheiven (10/15/2025)**

  - Calendar view component> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

- [x] **Main.java Integration**

- [ ] **Club/Organization Profiles**  - ✅ Added remote repository support with configuration helpers

  - Enhanced User model for organizations  - ✅ Implemented shouldUseRemoteRepository(), resolveRemoteUrl(), resolvePollInterval()

  - Club-specific posting privileges  - ✅ Wire up RemotePostSyncClient for polling

  - ✅ Added WindowAdapter for clean sync client shutdown

### ✅ Validation & Error Handling  - ✅ Initialize repositories and controllers

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**  - ✅ Location: `src/main/java/org/campusboard/sgs/Main.java`



- [ ] **Create PostValidator.java**- [x] **Category.java Enhancement**

  - Title length (1-200 characters)  - ✅ Added CAMPUS_SERVICES enum value for demo data

  - Body length (1-5000 characters)  - ✅ Location: `src/main/java/org/campusboard/sgs/model/Category.java`

  - Content moderation rules

  - Location: `src/main/java/org/campusboard/sgs/validation/`- [x] **TopBar.java Authentication Fix**

  - ✅ Added missing icon fields (guestAvatarIcon, userAvatarIcon, loginDoorIcon, logoutDoorIcon)

### 💅 UI Polish  - ✅ Created userInfoPanel and initialized in createComponents()

> *Roadmap Alignment*: Milestone **Real-time Sync & Notifications**  - ✅ Replaced simple username prompt with username/password dialog

  - ✅ Integrated Controller.AuthenticationResult for proper auth flow

- [ ] **Add Icons and Styling**  - ✅ Added secure password clearing after authentication attempts

  - Like/dislike icons  - ✅ Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`

  - Category badges with colors

  - User type indicators## 🔧 **Medium Priority - Enhancements**



- [ ] **Responsive Design**### Authentication System

  - Window resizing support> *Roadmap Alignment*: Milestone **Authentication Foundation**

  - Mobile-friendly layouts- [ ] **TODO: Create LoginDialog.java**

  - Username/password form

---  - Integration with UserRepository

  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/LoginDialog.java`

## 📋 **Implementation Guidelines**

### Search & Filter Enhancements

### Code Structure> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience** and feeds into **Real-time Sync & Notifications**

- **Framework Owner** (You): Creates class structures, method signatures, TODOs- [ ] **TODO: Create SearchService.java**

- **Implementation Team**: Fills in method bodies following TODO comments  - Advanced search functionality

- All UI components should extend appropriate Swing classes  - Search by title, body, author, category

- Use dependency injection pattern throughout  - Location: `src/main/java/org/campusboard/sgs/service/SearchService.java`



### Event System- [ ] **TODO: Add more FilterStrategy implementations**

- Publish events for all user actions using EventBus  - `DateRangeFilter.java` - filter by creation date

- Subscribe to events in view components for UI updates  - `AuthorFilter.java` - filter by post author  

- Follow existing AppEvent enum patterns  - `PopularityFilter.java` - filter by likes/engagement

  - Location: `src/main/java/org/campusboard/sgs/filter/`

### Campus Board Specific Requirements

- Support multiple user types (students, staff, clubs, etc.)### Command Pattern for Undo/Redo

- Category-based organization for campus content> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

- Search and filter capabilities for large amounts of posts- [ ] **TODO: Create Post Commands**

- Simple but effective UI suitable for daily campus use  - `CreatePostCommand.java` - implements Command interface

  - `DeletePostCommand.java` - implements Command interface

### Data Flow  - Wire up with UndoManager in Controller

```  - Location: `src/main/java/org/campusboard/sgs/controller/commands/`

User Action → View → Controller → Repository → Model

                ↓## 🎨 **Low Priority - Polish & Features**

            EventBus → Update Other Views

```### Campus-Specific Features

> *Roadmap Alignment*: Milestone **Real-time Sync & Notifications**

---- [ ] **TODO: Event Calendar Integration**

  - Special handling for Category.EVENTS posts

## ✅ **Completed Features**  - Calendar view component

  

### 🎨 View Layer (UI Implementation) - **COMPLETED BY DEO (10/14/2025)**- [ ] **TODO: Club/Organization Profiles**

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**  - Enhanced User model for organizations

  - Club-specific posting privileges

- [x] **MainWindow.java**

  - ✅ Layout implementation complete with BorderLayout### Validation & Error Handling

  - ✅ Component wiring and event handling implemented> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**

  - ✅ Thread-safe UI updates with SwingUtilities.invokeLater- [ ] **TODO: Create PostValidator.java**

  - ✅ EventBus integration ready (waiting on backend update)  - Title length (1-200 characters)

  - Location: `src/main/java/org/campusboard/sgs/view/MainWindow.java`  - Body length (1-5000 characters)

  - Content moderation rules

- [x] **TopBar.java**  - Location: `src/main/java/org/campusboard/sgs/validation/`

  - ✅ Search functionality with real-time filtering implemented

  - ✅ Create post button with dialog integration### UI Polish

  - ✅ Login/logout UI controls ready (waiting on Controller methods)> *Roadmap Alignment*: Milestone **Real-time Sync & Notifications**

  - ✅ FAU branding and professional styling- [ ] **TODO: Add Icons and Styling**

  - ✅ Hover effects and modern design patterns  - Like/dislike icons

  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`  - Category badges with colors

  - User type indicators

- [x] **FeedPanel.java**  

  - ✅ Scrollable posts display with BoxLayout- [ ] **TODO: Responsive Design**

  - ✅ Post filtering by category implemented  - Window resizing support

  - ✅ Search filtering implemented  - Mobile-friendly layouts

  - ✅ Dual filtering system (category + search)

  - ✅ Empty state handling## 📋 **Implementation Guidelines**

  - ✅ EventBus subscriptions ready (waiting on backend update)

  - Location: `src/main/java/org/campusboard/sgs/view/FeedPanel.java`### Code Structure

- **Framework Owner** (You): Creates class structures, method signatures, TODOs

- [x] **PostCard.java**- **Implementation Team**: Fills in method bodies following TODO comments

  - ✅ Professional post display with all fields- All UI components should extend appropriate Swing classes

  - ✅ Like/dislike button functionality implemented- Use dependency injection pattern throughout

  - ✅ Custom painted circular avatars

  - ✅ Human-readable timestamp formatting### Event System

  - ✅ Category badges with styling- Publish events for all user actions using EventBus

  - ✅ Delete button with confirmation- Subscribe to events in view components for UI updates

  - ✅ Hover effects and shadows- Follow existing AppEvent enum patterns

  - Location: `src/main/java/org/campusboard/sgs/view/PostCard.java`

### Campus Board Specific Requirements

- [x] **SidebarPanel.java**- Support multiple user types (students, staff, clubs, etc.)

  - ✅ Category filtering with blue selection system- Category-based organization for campus content

  - ✅ Quick access shortcuts- Search and filter capabilities for large amounts of posts

  - ✅ All category buttons with icons- Simple but effective UI suitable for daily campus use

  - ✅ Clear filters button

  - ✅ Hover effects### Data Flow

  - ✅ EventBus integration ready (waiting on backend update)```

  - Location: `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`User Action → View → Controller → Repository → Model

                ↓

- [x] **CreatePostDialog.java**            EventBus → Update Other Views

  - ✅ Form validation (title, body, category)```

  - ✅ Character limits and proper styling

  - ✅ Keyboard shortcuts (Enter to advance fields)## ✅ **Files Created & Ready for Implementation**

  - ✅ Error dialogs for validation- ✅ User.java (model structure complete)

  - ✅ Category dropdown with all options- ✅ UserType.java (enum complete)

  - Location: `src/main/java/org/campusboard/sgs/view/dialogs/CreatePostDialog.java`- ✅ UserRepository.java (interface complete)  

- ✅ InMemoryUserRepository.java (needs method implementation)

### 🔗 Main Application Wiring & Backend Integration - **COMPLETED BY kheiven (10/15/2025)**- ✅ Controller.java (methods signatures + TODOs)

> *Roadmap Alignment*: Milestone **Multi-user UI & Admin Experience**- ✅ All View classes (structure + TODOs)

- ✅ CreatePostDialog.java (structure + TODOs)

- [x] **Main.java Integration**- ✅ Main.java (wiring TODOs)

  - ✅ Added remote repository support with configuration helpers

  - ✅ Implemented shouldUseRemoteRepository(), resolveRemoteUrl(), resolvePollInterval()**Total TODO Items**: ~35 implementation tasks

  - ✅ Wire up RemotePostSyncClient for polling**Estimated Time**: 2-3 weeks for full implementation team

  - ✅ Added WindowAdapter for clean sync client shutdown
  - ✅ Initialize repositories and controllers
  - Location: `src/main/java/org/campusboard/sgs/Main.java`

- [x] **Category.java Enhancement**
  - ✅ Added CAMPUS_SERVICES enum value for demo data
  - Location: `src/main/java/org/campusboard/sgs/model/Category.java`

- [x] **TopBar.java Authentication Fix**
  - ✅ Added missing icon fields (guestAvatarIcon, userAvatarIcon, loginDoorIcon, logoutDoorIcon)
  - ✅ Created userInfoPanel and initialized in createComponents()
  - ✅ Replaced simple username prompt with username/password dialog
  - ✅ Integrated Controller.AuthenticationResult for proper auth flow
  - ✅ Added secure password clearing after authentication attempts
  - Location: `src/main/java/org/campusboard/sgs/view/TopBar.java`

### 📦 Files Created & Ready for Implementation

- ✅ User.java (model structure complete)
- ✅ UserType.java (enum complete)
- ✅ UserRepository.java (interface complete)
- ✅ InMemoryUserRepository.java (needs method implementation)
- ✅ Controller.java (methods signatures + TODOs)
- ✅ All View classes (structure + TODOs)
- ✅ CreatePostDialog.java (structure + TODOs)
- ✅ Main.java (wiring TODOs)

---

## 📊 **Project Stats**

- **Total TODO Items**: ~35 implementation tasks
- **Urgent Items**: 9 post-launch fixes
- **High Priority**: 3 core functionality items
- **Medium Priority**: 6 enhancement items
- **Low Priority**: 7 polish & feature items
- **Completed Components**: 6 major UI components + 3 backend integration pieces
- **Estimated Time**: 2-3 weeks for full implementation team
