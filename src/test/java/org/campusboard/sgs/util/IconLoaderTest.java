package org.campusboard.sgs.util;

import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconLoaderTest {

    @BeforeEach
    void clearCache() {
        IconLoader.clearCache();
    }

    @Test
    void categoryIconsAreAvailable() {
        assertIconsAvailable("categories", new String[] {
                "all",
                "announcements",
                "study-groups",
                "events",
                "lost-found",
                "trending"
        }, 24);
    }

    @Test
    void actionIconsAreAvailable() {
        assertIconsAvailable("actions", new String[] {
                "like-outline",
                "like-filled",
                "delete",
                "edit",
                "login",
                "logout"
        }, 16);
    }

    @Test
    void adminIconsAreAvailable() {
        assertIconsAvailable("admin", new String[] {
                "manage-users",
                "moderate",
                "reports"
        }, 16);
    }

    @Test
    void uiIconsAreAvailable() {
        assertIconsAvailable("ui", new String[] {
                "search",
                "user",
                "clock"
        }, 16);
    }

    private void assertIconsAvailable(String category, String[] names, int size) {
        for (String name : names) {
            assertTrue(IconLoader.exists(category, name),
                    () -> "Missing icon resource: " + category + "/" + name + ".png");
            ImageIcon icon = IconLoader.load(category, name, size);
            assertNotNull(icon, () -> "Failed to load icon " + category + "/" + name);
            assertTrue(icon.getIconWidth() > 0 && icon.getIconHeight() > 0,
                    () -> "Icon did not produce drawable dimensions for " + category + "/" + name);
        }
    }
}
