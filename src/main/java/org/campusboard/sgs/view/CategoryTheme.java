package org.campusboard.sgs.view;

import org.campusboard.sgs.model.Category;
import java.awt.Color;

/**
 * Defines color themes for post categories.
 * Each category has a distinct color for visual differentiation in badges and UI elements.
 */
public enum CategoryTheme {
  ANNOUNCEMENTS(new Color(255, 87, 51)),   // Coral red - urgent/important
  STUDY_GROUPS(new Color(52, 152, 219)),   // Bright blue - academic
  EVENTS(new Color(46, 204, 113)),         // Green - calendar/activities
  LOST_FOUND(new Color(241, 196, 15));     // Yellow/gold - attention

  private final Color badgeColor;

  CategoryTheme(Color badgeColor) {
    this.badgeColor = badgeColor;
  }

  /**
   * Get the badge background color for this category.
   * @return The color to use for category badges
   */
  public Color getBadgeColor() {
    return badgeColor;
  }

  /**
   * Get the theme for a specific category.
   * @param category The post category
   * @return The corresponding CategoryTheme
   */
  public static CategoryTheme forCategory(Category category) {
    return switch (category) {
      case ANNOUNCEMENTS -> ANNOUNCEMENTS;
      case STUDY_GROUPS -> STUDY_GROUPS;
      case EVENTS -> EVENTS;
      case LOST_FOUND -> LOST_FOUND;
    };
  }

  /**
   * Get a lighter shade of the badge color for hover effects.
   * @return A lighter version of the badge color
   */
  public Color getLighterShade() {
    return new Color(
        Math.min(255, badgeColor.getRed() + 30),
        Math.min(255, badgeColor.getGreen() + 30),
        Math.min(255, badgeColor.getBlue() + 30)
    );
  }

  /**
   * Get a darker shade of the badge color for active states.
   * @return A darker version of the badge color
   */
  public Color getDarkerShade() {
    return new Color(
        Math.max(0, badgeColor.getRed() - 30),
        Math.max(0, badgeColor.getGreen() - 30),
        Math.max(0, badgeColor.getBlue() - 30)
    );
  }
}
