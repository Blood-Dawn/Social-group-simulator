package org.campusboard.sgs.view;

import org.campusboard.sgs.model.Role;
import java.awt.Color;

/**
 * Defines color themes for user roles.
 * Each role has a distinct color for visual identification in badges and UI elements.
 */
public enum RoleTheme {
  GUEST(new Color(149, 165, 166), "G", "Guest User"),        // Gray - limited access
  STUDENT(new Color(52, 152, 219), "ST", "Student"),         // Blue - standard user
  STAFF(new Color(46, 204, 113), "S", "Staff Member"),       // Green - faculty/staff
  ADMIN(new Color(206, 17, 65), "A", "Administrator");       // FAU Red - full access

  private final Color themeColor;
  private final String badgeText;
  private final String displayName;

  RoleTheme(Color themeColor, String badgeText, String displayName) {
    this.themeColor = themeColor;
    this.badgeText = badgeText;
    this.displayName = displayName;
  }

  /**
   * Get the theme color for this role.
   * @return The color to use for role indicators
   */
  public Color getThemeColor() {
    return themeColor;
  }

  /**
   * Get the short text to display in role badges.
   * @return Short role indicator (e.g., "A" for Admin, "ST" for Student)
   */
  public String getBadgeText() {
    return badgeText;
  }

  /**
   * Get the full display name of the role.
   * @return Human-readable role name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Get the theme for a specific role.
   * @param role The user role
   * @return The corresponding RoleTheme
   */
  public static RoleTheme forRole(Role role) {
    return switch (role) {
      case GUEST -> GUEST;
      case STUDENT -> STUDENT;
      case STAFF -> STAFF;
      case ADMIN -> ADMIN;
    };
  }

  /**
   * Get a lighter shade of the theme color for hover effects.
   * @return A lighter version of the theme color
   */
  public Color getLighterShade() {
    return new Color(
        Math.min(255, themeColor.getRed() + 30),
        Math.min(255, themeColor.getGreen() + 30),
        Math.min(255, themeColor.getBlue() + 30)
    );
  }

  /**
   * Get a darker shade of the theme color for active states.
   * @return A darker version of the theme color
   */
  public Color getDarkerShade() {
    return new Color(
        Math.max(0, themeColor.getRed() - 30),
        Math.max(0, themeColor.getGreen() - 30),
        Math.max(0, themeColor.getBlue() - 30)
    );
  }

  /**
   * Determine if text on this color should be light (white) or dark (black).
   * Uses luminance calculation for accessibility.
   * @return Color.WHITE for dark backgrounds, Color.BLACK for light backgrounds
   */
  public Color getTextColor() {
    // Calculate relative luminance (perceived brightness)
    double luminance = (0.299 * themeColor.getRed() +
                       0.587 * themeColor.getGreen() +
                       0.114 * themeColor.getBlue()) / 255;

    // Use white text on dark backgrounds, black on light
    return luminance > 0.5 ? Color.BLACK : Color.WHITE;
  }
}
