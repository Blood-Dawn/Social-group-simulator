# Milestone 1: Application Requirements
**Campus Board - Social Group Simulator**

---

## 📋 **Project Information**

**Project Title**: Campus Board - Social Group Simulator  
**Platform**: Java Desktop Application with Swing GUI  
**Team Members**: [TO BE FILLED BY TEAM]  
**Group ID**: [TO BE ASSIGNED]  
**Submission Date**: October 1, 2025  

---

## 🎯 **Project Description**

Campus Board is a digital bulletin board application designed specifically for college campuses. It provides a centralized platform where students, faculty, staff, clubs, and organizations can share announcements, events, academic discussions, and campus-related content.

### **Intended Users**
- **Students**: Share posts, join discussions, find events and study groups
- **Faculty**: Post course announcements, academic content, and office hours
- **Staff**: Share administrative updates and campus services
- **Clubs/Organizations**: Announce events, recruit members, share updates
- **Administration**: Broadcast campus-wide announcements and policies
- **Alumni**: Network with current students and share mentorship opportunities

### **Platform Requirements**
- **Operating System**: Cross-platform (Windows, macOS, Linux)
- **Java Version**: Java 11 or higher
- **GUI Framework**: Java Swing
- **Build System**: Gradle
- **Architecture**: Desktop application with local data storage

---

## 📋 **Functional Specification**

### **1. User Management System**
1.1. User registration with email verification  
1.2. User authentication (login/logout)  
1.3. User profile management with personal information  
1.4. Multiple user types with different privileges  
1.5. Department/organization affiliation tracking  
1.6. User status management (active/inactive)  

### **2. Post Management System**  
2.1. Create new posts with title, body, and category  
2.2. View posts in chronological feed  
2.3. Edit own posts (with edit history)  
2.4. Delete own posts (with confirmation)  
2.5. Post categorization system  
2.6. Post timestamp and author tracking  
2.7. Character limits for titles and content  

### **3. Social Interaction Features**
3.1. Like/dislike posts with vote tracking  
3.2. View like/dislike counts on posts  
3.3. Prevent multiple votes per user per post  
3.4. Display user engagement statistics  

### **4. Content Discovery System**
4.1. Search posts by title and content  
4.2. Filter posts by category  
4.3. Filter posts by author type (student, faculty, etc.)  
4.4. Sort posts by date, popularity, or relevance  
4.5. Featured content promotion  
4.6. Trending posts identification  

### **5. User Interface Components**
5.1. Main application window with multi-panel layout  
5.2. Top navigation bar with search and user controls  
5.3. Sidebar with category filters and navigation  
5.4. Scrollable main feed displaying post cards  
5.5. Post creation dialog with form validation  
5.6. User login/profile dialogs  

### **6. System Administration**
6.1. Content moderation capabilities  
6.2. User management (activate/deactivate accounts)  
6.3. Category management  
6.4. System announcements  
6.5. Usage analytics and reporting  

### **7. Data Management**
7.1. Local data persistence  
7.2. Data backup and recovery  
7.3. Import/export functionality  
7.4. Database expansion capability  

---

## 📝 **Essential Use Cases**

### **UC1: Student Creates Post**
**Actor**: Student  
**Goal**: Share information with campus community  
**Precondition**: Student is logged into the system  
**Main Success Scenario**:
1. Student clicks "Create Post" button
2. System displays post creation dialog
3. Student enters post title
4. Student enters post content
5. Student selects appropriate category
6. Student clicks "Submit"
7. System validates input
8. System saves post to database
9. System displays success message
10. System updates main feed with new post

**Extensions**:
- 7a. Validation fails: System displays error message and returns to step 3
- 8a. Save fails: System displays error message and allows retry

### **UC2: User Views Feed**
**Actor**: Any authenticated user  
**Goal**: Browse campus posts and announcements  
**Precondition**: User is logged into the system  
**Main Success Scenario**:
1. User opens application
2. System displays main window
3. System loads recent posts from database
4. System displays posts in chronological order
5. User scrolls through feed
6. System loads additional posts as needed

### **UC3: User Filters by Category**
**Actor**: Any authenticated user  
**Goal**: View posts from specific category  
**Precondition**: User is viewing main feed  
**Main Success Scenario**:
1. User clicks category in sidebar
2. System filters posts by selected category
3. System updates feed display
4. System shows filtered post count
5. User views category-specific content

### **UC4: User Searches Posts**
**Actor**: Any authenticated user  
**Goal**: Find specific content  
**Precondition**: User is logged into the system  
**Main Success Scenario**:
1. User enters search terms in search box
2. User presses Enter or clicks search button
3. System searches post titles and content
4. System displays matching results
5. System highlights search terms in results
6. User views relevant posts

### **UC5: User Likes Post**
**Actor**: Any authenticated user  
**Goal**: Express approval for content  
**Precondition**: User is viewing a post  
**Main Success Scenario**:
1. User clicks "Like" button on post
2. System checks if user has already voted
3. System records like in database
4. System updates like count display
5. System disables like button for this user
6. System publishes like event

### **UC6: Faculty Posts Announcement**
**Actor**: Faculty member  
**Goal**: Share official course or academic information  
**Precondition**: Faculty member is logged into the system  
**Main Success Scenario**:
1. Faculty member clicks "Create Post" button
2. System displays post creation dialog
3. Faculty member enters announcement title
4. Faculty member enters announcement content
5. Faculty member selects "Announcements" category
6. System automatically marks post as "Official"
7. Faculty member clicks "Submit"
8. System validates input and faculty privileges
9. System saves announcement to database
10. System promotes post to featured content
11. System sends notifications to relevant users

### **UC7: Club Posts Event**
**Actor**: Club representative  
**Goal**: Announce upcoming club event  
**Precondition**: Club representative is logged into the system  
**Main Success Scenario**:
1. Club representative clicks "Create Post" button
2. System displays post creation dialog
3. Representative enters event title
4. Representative enters event details (date, time, location)
5. Representative selects "Events" category
6. Representative adds club affiliation
7. Representative clicks "Submit"
8. System validates input
9. System saves event post to database
10. System adds event to campus calendar integration

### **UC8: User Deletes Own Post**
**Actor**: Post author  
**Goal**: Remove previously created post  
**Precondition**: User is viewing their own post  
**Main Success Scenario**:
1. User clicks "Delete" button on their post
2. System displays confirmation dialog
3. User confirms deletion
4. System removes post from database
5. System updates feed display
6. System publishes post deletion event

---

## 📚 **Glossary**

**Announcement**: Official information shared by faculty or administration  
**Campus Board**: The main application interface displaying all posts  
**Category**: Classification system for organizing different types of content  
**Feed**: Chronological display of posts on the main interface  
**Like/Dislike**: User voting system to express approval or disapproval  
**Post**: Individual content item shared by users (text with title and body)  
**Post Card**: UI component displaying individual post information  
**User Type**: Classification of users (Student, Faculty, Staff, Club, etc.)  
**Featured Content**: Posts promoted by the system for increased visibility  
**Trending**: Posts with high engagement levels  
**Filter**: Mechanism to display subset of posts based on criteria  
**Search**: Text-based content discovery functionality  
**Event**: Time-specific announcements (meetings, activities, deadlines)  
**Club/Organization**: Groups that can post collectively under group identity  
**Engagement**: User interactions with posts (likes, views, responses)  
**Moderation**: Administrative control over content and users  

---

## 🎯 **Project Scope & Constraints**

### **In Scope**
- Desktop application for single-user local installation
- Core social features (post, like, filter, search)
- Multi-user type support with role-based content
- Category-based organization
- Local data persistence
- Basic content moderation features

### **Out of Scope**
- Real-time multi-user collaboration
- Mobile application versions
- Web-based interface
- Advanced multimedia content (videos, complex images)
- External social media integration
- Advanced analytics and reporting
- Cloud-based deployment

### **Technical Constraints**
- Must use Java with Swing GUI framework
- Local data storage (in-memory with file persistence)
- Single-machine deployment
- Desktop operating system compatibility

### **Business Constraints**
- Academic project timeline (semester-based)
- Team of four developers
- Educational focus on design patterns and OO principles
- Portfolio-ready demonstration quality

---

## 📊 **Success Criteria**

### **Functional Success**
- Users can create, view, edit, and delete posts
- Effective categorization and filtering system
- Search functionality finds relevant content
- Like/dislike system works accurately
- Multiple user types operate with appropriate privileges

### **Technical Success**
- Stable desktop application with professional UI
- Proper MVC architecture implementation
- At least 5 design patterns effectively utilized
- Comprehensive unit test coverage
- Complete JavaDoc documentation

### **Academic Success**
- Demonstrates mastery of OO design principles
- Showcases software engineering best practices
- Suitable for professional portfolio presentation
- Meets all course requirements and deadlines

---

**Document Status**: Requirements Specification Complete  
**Next Phase**: Design Specification (UML Diagrams)  
**Review Date**: [TO BE SCHEDULED WITH INSTRUCTOR]