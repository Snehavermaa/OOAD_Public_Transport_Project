package com.transport.transport_system.repository;

import com.transport.transport_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email
     *
     * @param email The email of the user
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email
     *
     * @param email The email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by role
     * @param role The role to filter by
     * @return List of users with the specified role
     */
    java.util.List<User> findByRole(String role);

    /**
     * Find active users by role
     * @param role The role to filter by
     * @param isActive The active status
     * @return List of active/inactive users with the specified role
     */
    java.util.List<User> findByRoleAndIsActive(String role, boolean isActive);
}
