package org.campusboard.sgs.model;

/**
 * Represents the source or category of content in the social group simulator.
 * Organized into logical groupings to reduce redundancy.
 */
public enum Source {
    // Content Publishers
    OFFICIAL,
    CLUB,
    USER,
    
    // System Generated
    SYSTEM,
    SPONSORED,
    RECOMMENDED,
    TRENDING,
    FEATURED,
    
    // Content Types
    ANNOUNCEMENT,
    EVENT,
    ALERT,
    NEWS,
    PROMOTION,
    SURVEY,
    FEEDBACK,
    TIP,
    REMINDER,
    INVITATION,
    
    // Gamification
    ACHIEVEMENT,
    BADGE,
    CHALLENGE,
    MILESTONE,
    
    // Social Interactions
    NOTIFICATION,
    MESSAGE,
    SOCIAL_ACTION, // covers COMMENT, LIKE, SHARE, FOLLOW, UNFOLLOW
    
    // Fallback
    OTHER
}
