package com.transport.transport_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transport.transport_system.factory.UserFactory;
import com.transport.transport_system.model.Admin;
import com.transport.transport_system.model.DriverUser;
import com.transport.transport_system.model.RegularUser;
import com.transport.transport_system.model.User;
import com.transport.transport_system.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ─── REGISTRATION ────────────────────────────────────────────────────────────

    /**
     * Register any user by role string (PASSENGER, DRIVER, ADMIN, CONDUCTOR).
     * Called directly from AuthController.
     */
    public User registerByRole(String role, String email, String password,
                               String firstName, String lastName, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        User newUser = UserFactory.createUser(role, email, password, firstName, lastName, phoneNumber);
        return userRepository.save(newUser);
    }

    /**
     * Register a passenger (RegularUser) — kept for backward compat.
     */
    public RegularUser registerPassenger(String email, String password, String firstName,
                                         String lastName, String phoneNumber,
                                         String address, String city, String zipCode) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        RegularUser newUser = UserFactory.createRegularUser(
                email, password, firstName, lastName, phoneNumber, address, city, zipCode);
        return userRepository.save(newUser);
    }

    /**
     * Register a driver.
     */
    public DriverUser registerDriver(String email, String password, String firstName,
                                     String lastName, String phoneNumber, String licenseNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        DriverUser newDriver = UserFactory.createDriverUser(
                email, password, firstName, lastName, phoneNumber, licenseNumber);
        return userRepository.save(newDriver);
    }

    /**
     * Register an admin.
     */
    public Admin registerAdmin(String email, String password, String firstName,
                               String lastName, String phoneNumber,
                               String department, String permissions) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        Admin newAdmin = UserFactory.createAdmin(
                email, password, firstName, lastName, phoneNumber, department, permissions);
        return userRepository.save(newAdmin);
    }

    /**
     * Legacy method — kept so existing code doesn't break.
     * Creates a PASSENGER (RegularUser) by default.
     */
    public User registerUser(String email, String password, String firstName,
                             String lastName, String phoneNumber) {
        return registerByRole("PASSENGER", email, password, firstName, lastName, phoneNumber);
    }

    // ─── LOOKUP ──────────────────────────────────────────────────────────────────

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public List<User> getActiveUsersByRole(String role) {
        return userRepository.findByRoleAndIsActive(role, true);
    }

    // ─── UPDATE / DELETE ─────────────────────────────────────────────────────────

    public User updateUser(Long id, String firstName, String lastName, String phoneNumber) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }

    public User deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setActive(false);
        return userRepository.save(user);
    }

    public User activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setActive(true);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}