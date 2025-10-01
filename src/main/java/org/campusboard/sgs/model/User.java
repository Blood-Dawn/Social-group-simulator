package org.campusboard.sgs.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a user in the campus board system.
 * Can be students, staff, clubs, organizations, etc.
 */
public class User {
    private UUID id;
    private String username;
    private String email;
    private String displayName;
    private UserType userType;
    private String department; // For staff/faculty
    private String organization; // For clubs/orgs
    private LocalDateTime joinDate;
    private boolean isActive;

    public User(String username, String email, String displayName, UserType userType) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.userType = userType;
        this.joinDate = LocalDateTime.now();
        this.isActive = true;
    }

    // TODO: Add all getters and setters
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public UserType getUserType() { return userType; }
    public String getDepartment() { return department; }
    public String getOrganization() { return organization; }
    public LocalDateTime getJoinDate() { return joinDate; }
    public boolean isActive() { return isActive; }
    
    // TODO: Add setters for mutable fields
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setDepartment(String department) { this.department = department; }
    public void setOrganization(String organization) { this.organization = organization; }
    public void setActive(boolean active) { this.isActive = active; }
}