# Campus Board - UI & Icon Implementation Plan
**Comprehensive Design Guide**

**Created**: November 12, 2025
**Status**: Planning Phase
**Priority**: Medium (Task 8 from UNCOMPLETED-TASKS.md)

---

## 📋 Overview

This document outlines the complete UI enhancement and icon implementation strategy for Campus Board, addressing the current emoji-based placeholder icons with proper vector assets and adding professional visual polish.

**Current State**:
- Emoji icons used for like (♥), delete (🗑), search (🔍)
- TODO comments added in PostCard.java:139, PostCard.java:154, TopBar.java:102
- No category icons in sidebar
- No role badges or indicators
- Basic color scheme (FAU Navy + FAU Red)

**Target State**:
- Professional vector icons (SVG/PNG) across all UI elements
- Consistent visual language
- Role-based theming and indicators
- Enhanced accessibility with tooltips
- Polished, modern appearance

---

## 🎨 1. Icon Inventory & Requirements

### A. Sidebar Category Buttons (SidebarPanel.java)
**Current**: Text-only filter buttons
**Target**: Icon + text buttons

| Category | Current | Icon Needed | Suggested Design |
|----------|---------|-------------|------------------|
| All Posts | Text only | Grid/Layout | 3x3 grid icon |
| Announcements | Text only | Megaphone | Megaphone or bullhorn |
| Study Groups | Text only | Book/Group | Open book or study group silhouette |
| Events | Text only | Calendar | Calendar page or event ticket |
| Lost & Found | Text only | Search/Key | Magnifying glass or key icon |
| Trending | Text only | Fire/Arrow | Flame or upward trending arrow |

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/SidebarPanel.java`

**Icon Specs**:
- Format: SVG (preferred) or PNG with transparency
- Size: 24x24px or 32x32px
- Color: FAU Navy (#003366) with hover state in FAU Red (#CE1141)
- Style: Material Design or Font Awesome

---

### B. Top Bar Icons (TopBar.java)

| Element | Current | Icon Needed | Location |
|---------|---------|-------------|----------|
| Search Field | 🔍 emoji | Magnifying glass | TopBar.java:103 (TODO added) |
| Login Button | Text only | User silhouette | Add icon |
| Logout Button | Text only | Door/arrow | Add icon |
| App Logo | "CB" text badge | Custom SVG | TopBar.java:48-96 (already implemented as circular badge) |

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/TopBar.java:102-104` (search icon)
- `src/main/java/org/campusboard/sgs/view/TopBar.java:99-107` (login button)
- `src/main/java/org/campusboard/sgs/view/TopBar.java:114-125` (logout button)

**Icon Specs**:
- Search: 16x16px, gray color
- Login/Logout: 16x16px, match button color scheme
- Logo: Already implemented as custom-drawn circular badge

---

### C. Post Card Icons (PostCard.java)

| Element | Current | Icon Needed | Location | Status |
|---------|---------|-------------|----------|--------|
| Like Button | ♥ emoji | Heart outline/filled | PostCard.java:140 | TODO added ✅ |
| Delete Button | 🗑 emoji | Trash can | PostCard.java:155 | TODO added ✅ |
| Category Badge | Text only | Category icon | Add to header | Planned |
| Author Avatar | Text only | Initials badge | Add to header | Planned |
| Timestamp Icon | None | Clock (optional) | PostCard.java:54 | Optional |

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/PostCard.java:126-147` (like button)
- `src/main/java/org/campusboard/sgs/view/PostCard.java:154-177` (delete button)
- `src/main/java/org/campusboard/sgs/view/PostCard.java:75-104` (header section for badges)

**Icon Specs**:
- Like: 16x16px, gray (not liked) → red (liked)
- Delete: 16x16px, FAU Red
- Category: 12x12px, color-coded by category
- Avatar: 32x32px circular badge with initials

**Enhanced Like Button States**:
```java
// Current state (no visual indication if user has liked)
likeButton.setText("♥ " + post.likeCount());

// Proposed state (visual feedback)
if (post.isLikedBy(session.user().username())) {
  likeButton.setIcon(heartFilledIcon);
  likeButton.setForeground(FAU_RED);
} else {
  likeButton.setIcon(heartOutlineIcon);
  likeButton.setForeground(Color.GRAY);
}
```

---

### D. Admin Menu Icons (MainWindow.java)

| Menu Item | Current | Icon Needed | Location |
|-----------|---------|-------------|----------|
| Manage Users | Text only | Users/group | MainWindow.java:82 |
| Moderate Posts | Text only | Gavel/shield | MainWindow.java:85 |
| View Reports | Text only | Bar chart | MainWindow.java:88 |

**Files to Modify**:
- `src/main/java/org/campusboard/sgs/view/MainWindow.java:81-94`

**Icon Specs**:
- Size: 16x16px
- Color: FAU Navy
- Style: Consistent with other menu icons

---

### E. Dialog Icons

| Dialog | Icons Needed | Usage |
|--------|--------------|-------|
| CreatePostDialog | Category icons | Category dropdown |
| LoginDialog | User icon | Dialog title |
| Confirmation Dialogs | Checkmark, X | Confirm/Cancel |
| ManageUsersDialog | Role badges | User list |
| ModeratePostsDialog | Moderation icons | Action buttons |

---

## 🎨 2. Color Scheme & Category Theming

### Category Color Palette

```java
public enum CategoryTheme {
  ANNOUNCEMENTS(new Color(255, 87, 51)),   // Coral red
  STUDY_GROUPS(new Color(52, 152, 219)),   // Bright blue
  EVENTS(new Color(46, 204, 113)),         // Green
  LOST_FOUND(new Color(241, 196, 15));     // Yellow/gold

  private final Color badgeColor;

  CategoryTheme(Color badgeColor) {
    this.badgeColor = badgeColor;
  }

  public Color getBadgeColor() { return badgeColor; }
}
```

**Implementation Location**:
- New file: `src/main/java/org/campusboard/sgs/view/CategoryTheme.java`
- Usage in: `PostCard.java:99` (category badge background)

---

### Role Color Palette

```java
public enum RoleTheme {
  GUEST(new Color(149, 165, 166)),    // Gray
  STUDENT(new Color(52, 152, 219)),   // Blue (matches FAU theme)
  STAFF(new Color(46, 204, 113)),     // Green
  ADMIN(new Color(206, 17, 65));      // FAU Red

  private final Color themeColor;

  RoleTheme(Color themeColor) {
    this.themeColor = themeColor;
  }

  public Color getThemeColor() { return themeColor; }
}
```

**Implementation Location**:
- New file: `src/main/java/org/campusboard/sgs/view/RoleTheme.java`
- Usage in: `TopBar.java:134-136` (username label color)
- Usage in: `PostCard.java` (author badge background)

---

## 🎯 3. Implementation Phases

### Phase 1: Icon Asset Preparation (1-2 hours)
**Priority**: HIGH

**Tasks**:
1. Create icon resources directory structure:
   ```
   src/main/resources/
   └── icons/
       ├── actions/
       │   ├── like-outline.png (16x16, 24x24, 32x32)
       │   ├── like-filled.png
       │   ├── delete.png
       │   ├── edit.png
       │   ├── login.png
       │   └── logout.png
       ├── categories/
       │   ├── announcements.png (24x24)
       │   ├── study-groups.png
       │   ├── events.png
       │   ├── lost-found.png
       │   ├── trending.png
       │   └── all.png
       ├── admin/
       │   ├── manage-users.png (16x16)
       │   ├── moderate.png
       │   └── reports.png
       └── ui/
           ├── search.png (16x16)
           ├── user.png
           └── clock.png
   ```

2. Source icons from:
   - **Material Design Icons**: https://fonts.google.com/icons
   - **Font Awesome**: https://fontawesome.com/icons
   - **Feather Icons**: https://feathericons.com/
   - **Custom SVG**: Design in Figma or Adobe Illustrator

3. Export requirements:
   - PNG format with transparency
   - Multiple sizes: 16x16, 24x24, 32x32
   - Or single SVG (scalable)

**Acceptance Criteria**:
- [ ] All 20+ icons sourced and exported
- [ ] Icons follow consistent style guide
- [ ] Icons placed in correct resource directories
- [ ] All icons have transparent backgrounds

---

### Phase 2: Icon Loading Utility (30 minutes)
**Priority**: HIGH

Create a centralized icon loading utility to avoid code duplication.

**New File**: `src/main/java/org/campusboard/sgs/util/IconLoader.java`

```java
package org.campusboard.sgs.util;

import javax.swing.*;
import java.net.URL;

public class IconLoader {
  private static final String ICON_PATH = "/icons/";

  public static ImageIcon load(String category, String name, int size) {
    String path = ICON_PATH + category + "/" + name + ".png";
    URL url = IconLoader.class.getResource(path);

    if (url == null) {
      System.err.println("Icon not found: " + path);
      return null;
    }

    ImageIcon icon = new ImageIcon(url);
    if (size > 0) {
      return new ImageIcon(icon.getImage().getScaledInstance(
          size, size, java.awt.Image.SCALE_SMOOTH));
    }
    return icon;
  }

  // Convenience methods
  public static ImageIcon loadAction(String name, int size) {
    return load("actions", name, size);
  }

  public static ImageIcon loadCategory(String name, int size) {
    return load("categories", name, size);
  }

  public static ImageIcon loadAdmin(String name, int size) {
    return load("admin", name, size);
  }

  public static ImageIcon loadUI(String name, int size) {
    return load("ui", name, size);
  }
}
```

**Usage Example**:
```java
// Before (with emoji)
JButton likeButton = new JButton("♥ 0");

// After (with icon)
ImageIcon likeIcon = IconLoader.loadAction("like-outline", 16);
JButton likeButton = new JButton(likeIcon);
likeButton.setText("0");
```

**Acceptance Criteria**:
- [ ] IconLoader utility created
- [ ] Handles missing icons gracefully
- [ ] Supports icon scaling
- [ ] All icon categories have convenience methods

---

### Phase 3: Replace Emoji Icons (2-3 hours)
**Priority**: MEDIUM

**Tasks**:

#### 3A. PostCard Like Button (PostCard.java:139-151)
```java
// Remove TODO comment
// Load both icon states
private final ImageIcon likeOutlineIcon = IconLoader.loadAction("like-outline", 16);
private final ImageIcon likeFilledIcon = IconLoader.loadAction("like-filled", 16);

// In createFooter():
likeButton = new JButton();
likeButton.setFont(new Font("Arial", Font.PLAIN, 13));
likeButton.setForeground(Color.GRAY);
likeButton.setBackground(CARD_BG);
likeButton.setBorderPainted(false);
likeButton.setFocusPainted(false);
likeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
likeButton.addActionListener(e -> controller.toggleLike(post));

// In bind():
boolean isLiked = post.isLikedBy(session.user().username());
likeButton.setIcon(isLiked ? likeFilledIcon : likeOutlineIcon);
likeButton.setText(" " + post.likeCount());
likeButton.setForeground(isLiked ? FAU_RED : Color.GRAY);
```

#### 3B. PostCard Delete Button (PostCard.java:154-177)
```java
// Remove TODO comment
ImageIcon deleteIcon = IconLoader.loadAction("delete", 16);
deleteButton = new JButton("Delete", deleteIcon);
// ... rest remains the same
```

#### 3C. TopBar Search Icon (TopBar.java:102-104)
```java
// Remove TODO comment
ImageIcon searchIcon = IconLoader.loadUI("search", 16);
JLabel searchIconLabel = new JLabel(searchIcon);
```

**Acceptance Criteria**:
- [ ] All emoji icons replaced with ImageIcon
- [ ] Like button shows filled/outline state
- [ ] Icons scale properly on different displays
- [ ] All TODO comments removed

---

### Phase 4: Add Category Icons (1-2 hours)
**Priority**: MEDIUM

#### 4A. Sidebar Filter Buttons (SidebarPanel.java)
```java
// Update createFilterButton() method
private JButton createFilterButton(String text, FilterStrategy strategy) {
  // Load category icon
  ImageIcon icon = switch(text) {
    case "All" -> IconLoader.loadCategory("all", 24);
    case "Announcements" -> IconLoader.loadCategory("announcements", 24);
    case "Study Groups" -> IconLoader.loadCategory("study-groups", 24);
    case "Events" -> IconLoader.loadCategory("events", 24);
    case "Lost & Found" -> IconLoader.loadCategory("lost-found", 24);
    case "Trending" -> IconLoader.loadCategory("trending", 24);
    default -> null;
  };

  JButton btn = new JButton(text, icon);
  btn.setHorizontalAlignment(SwingConstants.LEFT);
  btn.setIconTextGap(10);
  // ... rest of styling
}
```

#### 4B. Category Badges in PostCard (PostCard.java:94-100)
```java
// In createHeader():
ImageIcon categoryIcon = getCategoryIcon(post.category());
categoryBadge = new JLabel(formatCategory(cat), categoryIcon, JLabel.LEFT);
categoryBadge.setIconTextGap(5);
// ... rest of styling

private ImageIcon getCategoryIcon(Category cat) {
  return switch(cat) {
    case ANNOUNCEMENTS -> IconLoader.loadCategory("announcements", 12);
    case STUDY_GROUPS -> IconLoader.loadCategory("study-groups", 12);
    case EVENTS -> IconLoader.loadCategory("events", 12);
    case LOST_FOUND -> IconLoader.loadCategory("lost-found", 12);
  };
}
```

**Acceptance Criteria**:
- [ ] Sidebar buttons show category icons
- [ ] Post cards show small category icons in badges
- [ ] Icons align properly with text
- [ ] Consistent sizing across UI

---

### Phase 5: Role Badges & Theming (3-4 hours)
**Priority**: LOW (Task 4 from roadmap)

#### 5A. Author Role Badges in PostCard
```java
// In createHeader():
JPanel authorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
authorPanel.setOpaque(false);

// Role badge
JLabel roleBadge = createRoleBadge(authorRole);
authorPanel.add(roleBadge);

// Username
authorLabel = new JLabel("@author");
authorPanel.add(authorLabel);

left.add(authorPanel);

// Helper method
private JLabel createRoleBadge(Role role) {
  String text = switch(role) {
    case ADMIN -> "A";
    case STAFF -> "S";
    case STUDENT -> "ST";
    case GUEST -> "G";
  };

  JLabel badge = new JLabel(text);
  badge.setFont(new Font("Arial", Font.BOLD, 10));
  badge.setForeground(Color.WHITE);
  badge.setOpaque(true);
  badge.setBackground(RoleTheme.valueOf(role.name()).getThemeColor());
  badge.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
  return badge;
}
```

#### 5B. Top Bar Role Theming (TopBar.java)
```java
// In updateAuthUI():
if (session.isAuthenticated()) {
  Role role = session.user().role();
  Color roleColor = RoleTheme.valueOf(role.name()).getThemeColor();

  usernameLabel.setText("Welcome, " + session.user().username());
  usernameLabel.setForeground(roleColor);

  // Add role badge
  JLabel roleBadge = new JLabel(role.toString());
  roleBadge.setFont(new Font("Arial", Font.BOLD, 10));
  roleBadge.setForeground(Color.WHITE);
  roleBadge.setOpaque(true);
  roleBadge.setBackground(roleColor);
  roleBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

  authPanel.add(usernameLabel);
  authPanel.add(roleBadge);
  authPanel.add(logoutButton);
}
```

**Acceptance Criteria**:
- [ ] Role badges visible on post cards
- [ ] Color-coded by role (Admin=red, Staff=green, Student=blue, Guest=gray)
- [ ] Top bar shows user's role with color coding
- [ ] Tooltips explain role privileges

---

### Phase 6: Additional Polish (2-3 hours)
**Priority**: LOW

#### 6A. Enhanced Hover Effects
```java
// Add to all buttons
button.addMouseListener(new java.awt.event.MouseAdapter() {
  public void mouseEntered(java.awt.event.MouseEvent e) {
    button.setBackground(button.getBackground().brighter());
  }
  public void mouseExited(java.awt.event.MouseEvent e) {
    button.setBackground(originalColor);
  }
});
```

#### 6B. Rounded Corners for Cards
```java
// In PostCard constructor:
setBorder(new RoundedBorder(10, new Color(220, 220, 220)));

// New utility class
class RoundedBorder extends AbstractBorder {
  private int radius;
  private Color color;

  RoundedBorder(int radius, Color color) {
    this.radius = radius;
    this.color = color;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(color);
    g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    g2d.dispose();
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return new Insets(radius/2, radius/2, radius/2, radius/2);
  }
}
```

#### 6C. Tooltip Consistency
```java
// Add tooltips to all interactive elements
likeButton.setToolTipText("Like this post");
deleteButton.setToolTipText("Delete this post (admin only)");
searchField.setToolTipText("Search posts by title, content, or author");
```

**Acceptance Criteria**:
- [ ] All buttons have hover effects
- [ ] Cards have subtle rounded corners
- [ ] All interactive elements have descriptive tooltips
- [ ] Animations are smooth and performant

---

## 📊 4. Testing Checklist

### Icon Loading Tests
- [ ] All icons load without errors
- [ ] Missing icons handled gracefully (no crashes)
- [ ] Icons scale properly at different sizes
- [ ] Icons display correctly on Windows, Mac, Linux

### Visual Consistency Tests
- [ ] All icons follow same style guide
- [ ] Colors match FAU branding
- [ ] Spacing and padding consistent
- [ ] Icons align with text properly

### Interaction Tests
- [ ] Like button toggles between outline/filled
- [ ] Hover effects work on all buttons
- [ ] Tooltips display on all interactive elements
- [ ] Role badges display correct colors

### Accessibility Tests
- [ ] All icons have alt text / tooltips
- [ ] Color contrast meets WCAG AA standards
- [ ] Icons are distinguishable in grayscale
- [ ] Keyboard navigation works with icons

---

## 🚀 5. Priority Recommendations

### Must Have (Phase 1-3)
1. ✅ Icon asset preparation and directory structure
2. ✅ IconLoader utility class
3. ✅ Replace emoji icons (like, delete, search)
4. ⏭️ Add category icons to sidebar

### Should Have (Phase 4-5)
5. ⏭️ Category icons in post cards
6. ⏭️ Role badges and theming
7. ⏭️ Admin menu icons

### Nice to Have (Phase 6)
8. ⏭️ Enhanced hover effects
9. ⏭️ Rounded corners
10. ⏭️ Animation polish

---

## 📝 6. Implementation Notes

### Icon Format Decision
**Recommendation**: Use PNG with multiple sizes

**Rationale**:
- SVG support in Swing requires additional libraries
- PNG provides good quality at fixed sizes
- Multiple sizes (16x16, 24x24, 32x32) cover all use cases
- Transparent background ensures flexibility

### Color Theming Architecture
**Recommendation**: Create theme enums (CategoryTheme, RoleTheme)

**Benefits**:
- Centralized color definitions
- Easy to update theme colors
- Type-safe color selection
- Consistent across application

### Performance Considerations
- Load icons once and cache them (use static fields or singleton)
- Don't reload icons on every paint
- Use scaled instances instead of scaling on every render

### Accessibility
- Always provide text alternatives (tooltips)
- Ensure 4.5:1 contrast ratio for text and icons
- Test with screen readers
- Support keyboard navigation

---

## 📚 7. Resources

### Icon Sources
- **Material Design Icons**: https://fonts.google.com/icons (Free, Apache 2.0)
- **Font Awesome**: https://fontawesome.com/icons (Free tier available)
- **Feather Icons**: https://feathericons.com/ (MIT License)
- **Heroicons**: https://heroicons.com/ (MIT License)

### Design Tools
- **Figma**: For custom icon design
- **Adobe Illustrator**: Professional vector editing
- **Inkscape**: Free SVG editor
- **GIMP**: Free raster image editor

### Color Tools
- **Coolors**: Color palette generator
- **WebAIM Contrast Checker**: Accessibility testing
- **Adobe Color**: Color scheme creation

---

## 🔄 8. Update Schedule

This document should be updated:
- ✅ After each phase completion
- ✅ When new icons are added
- ✅ When design decisions change
- ✅ After user feedback

**Last Updated**: November 12, 2025
**Next Review**: When Phase 1 begins
