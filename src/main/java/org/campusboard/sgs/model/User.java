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

    // Authentication & security metadata
    private String passwordHash;
    private String passwordSalt;
    private int failedLoginAttempts;
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastFailedLoginAt;
    private String securityNotes;

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
    public String getPasswordHash() { return passwordHash; }
    public String getPasswordSalt() { return passwordSalt; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public LocalDateTime getLastFailedLoginAt() { return lastFailedLoginAt; }
    public String getSecurityNotes() { return securityNotes; }

    // TODO: Add setters for mutable fields
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setDepartment(String department) { this.department = department; }
    public void setOrganization(String organization) { this.organization = organization; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public void setLastFailedLoginAt(LocalDateTime lastFailedLoginAt) { this.lastFailedLoginAt = lastFailedLoginAt; }
    public void setSecurityNotes(String securityNotes) { this.securityNotes = securityNotes; }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts += 1;
        this.lastFailedLoginAt = LocalDateTime.now();
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }
}