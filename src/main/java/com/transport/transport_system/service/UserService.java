package com.transport.transport_system.service;

import com.transport.transport_system.factory.UserFactory;
import com.transport.transport_system.model.Admin;
import com.transport.transport_system.model.RegularUser;
import com.transport.transport_system.model.User;
import com.transport.transport_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user (RegularUser by default)
     *
     * @param email Email of the user
     * @param password Password for the user
     * @param firstName First name
     * @param lastName Last name
     * @param phoneNumber Phone number
     * @return The saved user
     * @throws IllegalArgumentException if email already exists
     */
    public User registerUser(String email, String password, String firstName, 
                            String lastName, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        // Use factory pattern to create a regular user
        User newUser = UserFactory.createUser(
            UserFactory.UserType.REGULAR_USER.getValue(),
            email, password, firstName, lastName, phoneNumber
        );

        return userRepository.save(newUser);
    }

    /**
     * Register a new user with address details
     *
     * @param email Email of the user
     * @param password Password for the user
     * @param firstName First name
     * @param lastName Last name
     * @param phoneNumber Phone number
     * @param address Address
     * @param city City
     * @param zipCode Zip code
     * @return The saved user
     * @throws IllegalArgumentException if email already exists
     */
    public RegularUser registerUserWithAddress(String email, String password, String firstName,
                                               String lastName, String phoneNumber,
                                               String address, String city, String zipCode) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        RegularUser newUser = UserFactory.createRegularUser(
            email, password, firstName, lastName, phoneNumber, address, city, zipCode
        );

        return userRepository.save(newUser);
    }

    /**
     * Register a new admin user
     *
     * @param email Email of the admin
     * @param password Password for the admin
     * @param firstName First name
     * @param lastName Last name
     * @param phoneNumber Phone number
     * @param department Department
     * @param permissions Permissions
     * @return The saved admin user
     * @throws IllegalArgumentException if email already exists
     */
    public Admin registerAdmin(String email, String password, String firstName,
                              String lastName, String phoneNumber,
                              String department, String permissions) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        Admin newAdmin = UserFactory.createAdmin(
            email, password, firstName, lastName, phoneNumber, department, permissions
        );

        return userRepository.save(newAdmin);
    }

    /**
     * Get user by email
     *
     * @param email Email of the user
     * @return Optional containing the user if found
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by ID
     *
     * @param id User ID
     * @return Optional containing the user if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all users by role
     *
     * @param role The role (ADMIN or USER)
     * @return List of users with the specified role
     */
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    /**
     * Get all active users by role
     *
     * @param role The role (ADMIN or USER)
     * @return List of active users with the specified role
     */
    public List<User> getActiveUsersByRole(String role) {
        return userRepository.findByRoleAndIsActive(role, true);
    }

    /**
     * Update user information
     *
     * @param id User ID
     * @param firstName New first name
     * @param lastName New last name
     * @param phoneNumber New phone number
     * @return Updated user
     * @throws RuntimeException if user not found
     */
    public User updateUser(Long id, String firstName, String lastName, String phoneNumber) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = userOptional.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);

        return userRepository.save(user);
    }

    /**
     * Deactivate a user account
     *
     * @param id User ID
     * @return Updated user
     * @throws RuntimeException if user not found
     */
    public User deactivateUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = userOptional.get();
        user.setActive(false);

        return userRepository.save(user);
    }

    /**
     * Activate a user account
     *
     * @param id User ID
     * @return Updated user
     * @throws RuntimeException if user not found
     */
    public User activateUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = userOptional.get();
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Delete a user
     *
     * @param id User ID
     * @throws RuntimeException if user not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
