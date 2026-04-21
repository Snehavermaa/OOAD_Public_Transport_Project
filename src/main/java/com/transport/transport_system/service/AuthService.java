package com.transport.transport_system.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transport.transport_system.model.User;
import com.transport.transport_system.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticate user with email and password.
     * Works for all roles: PASSENGER, DRIVER, ADMIN, CONDUCTOR.
     */
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Plain-text comparison (replace with BCrypt in production)
            if (user.getPassword().equals(password) && user.isActive()) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    // ─── Role helpers ────────────────────────────────────────────────────────────

    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    public boolean isPassenger(User user) {
        return user != null && ("PASSENGER".equals(user.getRole()) || "USER".equals(user.getRole()));
    }

    public boolean isDriver(User user) {
        return user != null && "DRIVER".equals(user.getRole());
    }

    public boolean isConductor(User user) {
        return user != null && "CONDUCTOR".equals(user.getRole());
    }

    // ─── Validation ──────────────────────────────────────────────────────────────

    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) return false;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        return hasLetter && hasNumber;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    public boolean logout() {
        return true;
    }
}