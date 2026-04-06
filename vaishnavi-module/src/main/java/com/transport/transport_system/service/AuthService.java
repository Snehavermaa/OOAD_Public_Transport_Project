package com.transport.transport_system.service;

import com.transport.transport_system.model.User;
import com.transport.transport_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticate user with email and password
     *
     * @param email User's email
     * @param password User's password
     * @return Optional containing the user if authentication is successful
     */
    public Optional<User> authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            User foundUser = user.get();

            // Check if password matches (In production, use bcrypt or similar)
            if (foundUser.getPassword().equals(password) && foundUser.isActive()) {
                return Optional.of(foundUser);
            }
        }

        return Optional.empty();
    }

    /**
     * Check if user has admin role
     *
     * @param user The user to check
     * @return true if user is an admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    /**
     * Check if user has regular user role
     *
     * @param user The user to check
     * @return true if user is a regular user, false otherwise
     */
    public boolean isRegularUser(User user) {
        return user != null && "USER".equals(user.getRole());
    }

    /**
     * Validate password strength
     *
     * @param password Password to validate
     * @return true if password is strong, false otherwise
     */
    public boolean isPasswordStrong(String password) {
        // Password must be at least 6 characters
        if (password == null || password.length() < 6) {
            return false;
        }

        // Password should contain at least one letter and one number
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");

        return hasLetter && hasNumber;
    }

    /**
     * Validate email format
     *
     * @param email Email to validate
     * @return true if email format is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Logout user (in this case, just returns true)
     * In a real application, you might want to invalidate sessions/tokens
     *
     * @return true if logout is successful
     */
    public boolean logout() {
        return true;
    }
}
