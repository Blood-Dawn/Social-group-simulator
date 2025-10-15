package org.campusboard.sgs.controller;

/**
 * Application events for the event bus system.
 * Organized by functional areas for better maintainability.
 */
public enum AppEvent {
    // Post Management
    POST_CREATED,
    POST_UPDATED,
    POST_DELETED,
    POSTS_CHANGED, // bulk change event
    
    // User Interactions
    POST_LIKED,
    POST_DISLIKED,
    POST_SHARED,
    POST_REPORTED,
    
    // Navigation & Filtering
    FILTER_CHANGED,
    SEARCH_REQUESTED,
    CATEGORY_CHANGED,
    SORT_CHANGED,
    
    // UI State
    VIEW_CHANGED,
    THEME_CHANGED,
    SIDEBAR_TOGGLED,
    
    // User Management
    USER_LOGGED_IN,
    USER_LOGGED_OUT,
    USER_ROLE_CHANGED,
    PROFILE_UPDATED,
    
    // System Events
    DATA_LOADED,
    ERROR_OCCURRED,
    NOTIFICATION_RECEIVED
}
