package com.transport.transport_system.factory;

import com.transport.transport_system.model.Admin;
import com.transport.transport_system.model.RegularUser;
import com.transport.transport_system.model.User;

/**
 * Factory Pattern Implementation for User Creation
 * This factory is responsible for creating instances of different user types (Admin, RegularUser)
 * based on the user role provided.
 */
public class UserFactory {

    /**
     * Enum for User Types
     */
    public enum UserType {
        ADMIN("ADMIN"),
        REGULAR_USER("USER");

        private final String value;

        UserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static UserType fromValue(String value) {
            for (UserType type : UserType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid user type: " + value);
        }
    }

    /**
     * Create a user based on the specified type
     *
     * @param userType The type of user to create (ADMIN or USER)
     * @param email    User's email
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @param phoneNumber User's phone number
     * @return A new User object of the specified type
     * @throws IllegalArgumentException if userType is invalid
     */
    public static User createUser(String userType, String email, String password, 
                                   String firstName, String lastName, String phoneNumber) {
        UserType type = UserType.fromValue(userType);

        switch (type) {
            case ADMIN:
                return new Admin(email, password, firstName, lastName, phoneNumber);
            case REGULAR_USER:
                return new RegularUser(email, password, firstName, lastName, phoneNumber);
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }

    /**
     * Create an Admin user
     *
     * @param email Email of the admin
     * @param password Password for the admin
     * @param firstName First name of the admin
     * @param lastName Last name of the admin
     * @param phoneNumber Phone number of the admin
     * @param department Department of the admin
     * @param permissions Permissions for the admin
     * @return A new Admin instance
     */
    public static Admin createAdmin(String email, String password, String firstName, 
                                    String lastName, String phoneNumber, 
                                    String department, String permissions) {
        return new Admin(email, password, firstName, lastName, phoneNumber, department, permissions);
    }

    /**
     * Create a Regular User
     *
     * @param email Email of the user
     * @param password Password for the user
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param phoneNumber Phone number of the user
     * @param address Address of the user
     * @param city City of the user
     * @param zipCode Zip code of the user
     * @return A new RegularUser instance
     */
    public static RegularUser createRegularUser(String email, String password, String firstName,
                                                String lastName, String phoneNumber,
                                                String address, String city, String zipCode) {
        return new RegularUser(email, password, firstName, lastName, phoneNumber, address, city, zipCode);
    }
}
