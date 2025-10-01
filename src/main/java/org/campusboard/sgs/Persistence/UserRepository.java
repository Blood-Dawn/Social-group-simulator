package org.campusboard.sgs.Persistence;

import org.campusboard.sgs.model.User;
import java.util.*;

public interface UserRepository {
    // CRUD Operations
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User save(User user);
    User update(User user);
    boolean delete(UUID id);
    
    // TODO: Add authentication methods
    // TODO: boolean authenticate(String username, String password);
    // TODO: List<User> findByUserType(UserType userType);
    // TODO: List<User> findByDepartment(String department);
    // TODO: List<User> findByOrganization(String organization);
}