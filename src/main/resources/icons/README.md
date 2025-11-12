# Campus Board - Icon Resources

This directory contains icon assets for the Campus Board application.

## Directory Structure

```
icons/
├── actions/          # Action button icons (like, delete, edit, login, logout)
├── categories/       # Category filter icons (announcements, study-groups, events, etc.)
├── admin/           # Admin menu icons (manage-users, moderate, reports)
└── ui/              # UI element icons (search, user, clock)
```

## Icon Specifications

### Format
- **Format**: PNG with transparency (preferred) or SVG
- **Sizes**: 16x16px, 24x24px, 32x32px (multiple sizes for scaling)
- **Background**: Transparent
- **Style**: Material Design, Font Awesome, or Feather Icons (consistent style)

### Naming Convention
- Use kebab-case: `like-outline.png`, `study-groups.png`
- Include size suffix if multiple sizes: `like-outline-16.png`, `like-outline-24.png`
- Use descriptive names: `delete.png` not `trash.png`

## Required Icons

### actions/ (Action Buttons)
- [ ] `like-outline.png` - Heart outline for unliked state (16x16, 24x24)
- [ ] `like-filled.png` - Filled heart for liked state (16x16, 24x24)
- [ ] `delete.png` - Trash can icon (16x16, 24x24)
- [ ] `edit.png` - Pencil icon for editing (16x16, 24x24)
- [ ] `login.png` - User silhouette for login (16x16)
- [ ] `logout.png` - Door with arrow for logout (16x16)

### categories/ (Category Filters)
- [ ] `all.png` - Grid icon for all posts (24x24, 32x32)
- [ ] `announcements.png` - Megaphone icon (24x24, 32x32)
- [ ] `study-groups.png` - Book or group icon (24x24, 32x32)
- [ ] `events.png` - Calendar icon (24x24, 32x32)
- [ ] `lost-found.png` - Magnifying glass or key icon (24x24, 32x32)
- [ ] `trending.png` - Flame or arrow up icon (24x24, 32x32)

### admin/ (Admin Menu)
- [ ] `manage-users.png` - Users/group icon (16x16)
- [ ] `moderate.png` - Gavel or shield icon (16x16)
- [ ] `reports.png` - Bar chart icon (16x16)

### ui/ (UI Elements)
- [ ] `search.png` - Magnifying glass (16x16)
- [ ] `user.png` - User profile icon (16x16, 32x32)
- [ ] `clock.png` - Clock icon for timestamps (12x12, 16x16)

## Icon Sources

### Free Icon Libraries
1. **Material Design Icons**: https://fonts.google.com/icons
   - License: Apache 2.0
   - Format: SVG, PNG
   - Style: Clean, modern

2. **Font Awesome**: https://fontawesome.com/icons
   - License: Free tier available
   - Format: SVG, Web font
   - Style: Comprehensive collection

3. **Feather Icons**: https://feathericons.com/
   - License: MIT
   - Format: SVG
   - Style: Minimalist, lightweight

4. **Heroicons**: https://heroicons.com/
   - License: MIT
   - Format: SVG
   - Style: Modern, Tailwind CSS integration

## Installation Instructions

### Option 1: Download Individual Icons
1. Visit one of the icon libraries above
2. Search for the icon name (e.g., "heart", "trash", "calendar")
3. Download in PNG format (16x16, 24x24, 32x32)
4. Ensure transparent background
5. Place in appropriate subdirectory
6. Rename to match naming convention

### Option 2: Convert SVG to PNG
If you download SVG icons, convert them to PNG:
```bash
# Using ImageMagick
convert -background none -resize 16x16 icon.svg icon-16.png
convert -background none -resize 24x24 icon.svg icon-24.png
convert -background none -resize 32x32 icon.svg icon-32.png
```

### Option 3: Use Online Converters
- https://cloudconvert.com/svg-to-png
- https://convertio.co/svg-png/

## Usage in Code

Icons are loaded using the `IconLoader` utility class:

```java
// Load action icons
ImageIcon likeIcon = IconLoader.loadAction("like-outline", 16);
ImageIcon deleteIcon = IconLoader.loadAction("delete", 16);

// Load category icons
ImageIcon announcementsIcon = IconLoader.loadCategory("announcements", 24);

// Load admin icons
ImageIcon manageUsersIcon = IconLoader.loadAdmin("manage-users", 16);

// Load UI icons
ImageIcon searchIcon = IconLoader.loadUI("search", 16);
```

## Placeholder Icons

Until proper icon assets are added, the application uses:
- Emoji characters (♥, 🗑, 🔍) - marked with TODO comments
- Text-only buttons
- These should be replaced with proper icons for consistent cross-platform rendering

## Color Guidelines

Icons should be:
- **Neutral color** (gray, black, or theme color)
- **Transparent background**
- **Consistent stroke width** (2px recommended)
- **Simple and clear** at small sizes

The application will apply appropriate colors based on context:
- Action buttons: Gray → FAU Red on hover
- Category icons: Match CategoryTheme colors
- Role badges: Match RoleTheme colors

## Testing Checklist

After adding icons:
- [ ] All icons load without errors in IconLoader
- [ ] Icons display at correct sizes
- [ ] Icons are crisp on high-DPI displays
- [ ] Icons render consistently across platforms (Windows, Mac, Linux)
- [ ] Transparent backgrounds work correctly
- [ ] Icons are visible on both light and dark backgrounds
- [ ] Hover effects work as expected

## Related Documentation

- `docs/UI-ICON-IMPLEMENTATION-PLAN.md` - Complete implementation guide
- `docs/UNCOMPLETED-TASKS.md` - Task 8: Icon Assets
- `src/main/java/org/campusboard/sgs/util/IconLoader.java` - Icon loading utility

## Status

**Current Status**: Structure created, awaiting icon assets

**Next Steps**:
1. Download icons from chosen library (recommended: Material Design Icons)
2. Convert/resize to specified dimensions
3. Place in appropriate directories
4. Test with IconLoader utility
5. Replace emoji icons in code (see TODO comments in PostCard.java, TopBar.java)

---

*Last Updated*: November 12, 2025
