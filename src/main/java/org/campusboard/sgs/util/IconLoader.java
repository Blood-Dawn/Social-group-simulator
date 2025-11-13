package org.campusboard.sgs.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized utility for loading and caching icon resources.
 * Provides consistent icon loading across the application with error handling and caching.
 */
public class IconLoader {
  private static final String ICON_PATH = "/icons/";
  private static final Map<String, ImageIcon> iconCache = new HashMap<>();
  private static final boolean DEBUG = false; // Set to true to see icon loading messages

  /**
   * Load an icon from the specified category and name.
   * Icons are cached after first load for performance.
   *
   * @param category The icon category (actions, categories, admin, ui)
   * @param name The icon filename without extension
   * @param size The desired icon size (0 for original size)
   * @return The loaded ImageIcon, or null if not found
   */
  public static ImageIcon load(String category, String name, int size) {
    String cacheKey = category + "/" + name + "-" + size;

    // Return cached icon if available
    if (iconCache.containsKey(cacheKey)) {
      return iconCache.get(cacheKey);
    }

    // Try to load icon with size suffix first (e.g., like-outline-16.png)
    String pathWithSize = ICON_PATH + category + "/" + name + "-" + size + ".png";
    URL urlWithSize = IconLoader.class.getResource(pathWithSize);

    if (urlWithSize != null) {
      ImageIcon icon = new ImageIcon(urlWithSize);
      iconCache.put(cacheKey, icon);
      if (DEBUG) System.out.println("Loaded icon: " + pathWithSize);
      return icon;
    }

    // Fall back to base name without size (e.g., like-outline.png)
    String path = ICON_PATH + category + "/" + name + ".png";
    URL url = IconLoader.class.getResource(path);

    if (url == null) {
      if (DEBUG) System.err.println("Icon not found: " + path + " (or " + pathWithSize + ")");
      return null;
    }

    ImageIcon icon = new ImageIcon(url);

    // Scale if size is specified and different from original
    if (size > 0 && (icon.getIconWidth() != size || icon.getIconHeight() != size)) {
      Image scaledImage = icon.getImage().getScaledInstance(
          size, size, Image.SCALE_SMOOTH);
      icon = new ImageIcon(scaledImage);
    }

    iconCache.put(cacheKey, icon);
    if (DEBUG) System.out.println("Loaded and scaled icon: " + path + " to " + size + "px");
    return icon;
  }

  /**
   * Load an action icon (like, delete, edit, etc.).
   * @param name Icon name without extension
   * @param size Desired size in pixels
   * @return The loaded ImageIcon, or null if not found
   */
  public static ImageIcon loadAction(String name, int size) {
    return load("actions", name, size);
  }

  /**
   * Load a category icon (announcements, study-groups, etc.).
   * @param name Icon name without extension
   * @param size Desired size in pixels
   * @return The loaded ImageIcon, or null if not found
   */
  public static ImageIcon loadCategory(String name, int size) {
    return load("categories", name, size);
  }

  /**
   * Load an admin menu icon (manage-users, moderate, reports).
   * @param name Icon name without extension
   * @param size Desired size in pixels
   * @return The loaded ImageIcon, or null if not found
   */
  public static ImageIcon loadAdmin(String name, int size) {
    return load("admin", name, size);
  }

  /**
   * Load a UI element icon (search, user, clock).
   * @param name Icon name without extension
   * @param size Desired size in pixels
   * @return The loaded ImageIcon, or null if not found
   */
  public static ImageIcon loadUI(String name, int size) {
    return load("ui", name, size);
  }

  /**
   * Clear the icon cache. Useful for testing or if icons are updated at runtime.
   */
  public static void clearCache() {
    iconCache.clear();
    if (DEBUG) System.out.println("Icon cache cleared");
  }

  /**
   * Get the number of cached icons.
   * @return The cache size
   */
  public static int getCacheSize() {
    return iconCache.size();
  }

  /**
   * Check if an icon exists without loading it.
   * @param category The icon category
   * @param name The icon filename without extension
   * @return true if the icon resource exists
   */
  public static boolean exists(String category, String name) {
    String path = ICON_PATH + category + "/" + name + ".png";
    URL url = IconLoader.class.getResource(path);
    return url != null;
  }

  /**
   * Create a placeholder icon with the specified size and color.
   * Useful as a fallback when the actual icon is not available.
   *
   * @param size The icon size
   * @param color The icon color
   * @return A simple square placeholder icon
   */
  public static ImageIcon createPlaceholder(int size, Color color) {
    Image image = new ImageIcon().getImage();
    image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = (Graphics2D) image.getGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw a simple square with border
    g2d.setColor(color.brighter());
    g2d.fillRect(2, 2, size - 4, size - 4);
    g2d.setColor(color);
    g2d.drawRect(1, 1, size - 2, size - 2);

    g2d.dispose();
    return new ImageIcon(image);
  }

  /**
   * Load an icon with a fallback to placeholder if not found.
   * @param category The icon category
   * @param name The icon name
   * @param size The desired size
   * @param placeholderColor Color for placeholder if icon not found
   * @return The loaded icon or a placeholder
   */
  public static ImageIcon loadOrPlaceholder(String category, String name, int size, Color placeholderColor) {
    ImageIcon icon = load(category, name, size);
    if (icon == null) {
      if (DEBUG) System.out.println("Using placeholder for: " + category + "/" + name);
      return createPlaceholder(size, placeholderColor);
    }
    return icon;
  }
}
