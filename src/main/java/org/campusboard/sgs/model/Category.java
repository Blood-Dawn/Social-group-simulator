package org.campusboard.sgs.model;

/** 
 * Categories for posts - consolidated with source information
 * Combines content type, origin, and subject matter
 */
public enum Category {
    // Campus Essentials & Official Content
    ANNOUNCEMENTS,        // Official announcements (was Source.OFFICIAL + Category.ANNOUNCEMENTS)
    EVENTS,              // Campus events
    ACADEMICS,           // Academic content
    CAMPUS_LIFE,         // General campus life
    
    // System Generated Content
    FEATURED,            // Featured/promoted content (was Source.FEATURED)
    TRENDING,            // Trending posts (was Source.TRENDING)
    RECOMMENDED,         // Algorithm recommended (was Source.RECOMMENDED)
    
    // Student Services
    HOUSING,
    JOBS_INTERNSHIPS,
    BUY_SELL,
    LOST_FOUND,
    TRAVEL_TRANSPORTATION,
    
    // Social & Activities
    CLUBS_ORGS,          // Club/organization content (was Source.CLUB)
    STUDY_GROUPS,
    SPORTS_FITNESS,
    SOCIAL_EVENTS,
    VOLUNTEERING,
    
    // Lifestyle & Interests
    ARTS_ENTERTAINMENT,  // covers music, cinema, theater, dance, photography
    TECHNOLOGY,
    HEALTH_WELLNESS,
    FOOD_DRINKS,
    GAMING,
    
    // Academic Subjects
    STEM,               // covers mathematics, physics, engineering, medicine
    HUMANITIES,         // covers literature, philosophy, history, linguistics
    SOCIAL_SCIENCES,    // covers psychology, economics, law, geography
    
    // Social Interactions (was Source social actions)
    NOTIFICATIONS,      // System notifications
    MESSAGES,           // Direct messages
    SOCIAL_ACTIVITY,    // Likes, shares, follows, etc.
    
    // Content Types (was Source content types)
    SURVEYS,
    FEEDBACK,
    TIPS,
    REMINDERS,
    INVITATIONS,
    
    // Gamification (was Source gamification)
    ACHIEVEMENTS,
    BADGES,
    CHALLENGES,
    MILESTONES,
    
    // General
    GENERAL,
    HUMOR_MEMES,
    OFF_TOPIC,
    OTHER,
    MARKETPLACE("Marketplace"),
    LOST_AND_FOUND("Lost & Found");

    private final String displayName;

    Category() {
        this.displayName = formatDisplayName(name());
    }

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static String formatDisplayName(String rawName) {
        String lower = rawName.toLowerCase().replace('_', ' ');
        String[] tokens = lower.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            builder.append(Character.toUpperCase(token.charAt(0)))
                   .append(token.substring(1))
                   .append(' ');
        }
        return builder.toString().trim();
    }
}
