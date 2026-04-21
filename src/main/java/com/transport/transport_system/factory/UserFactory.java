package com.transport.transport_system.factory;

import com.transport.transport_system.model.Admin;
import com.transport.transport_system.model.ConductorUser;
import com.transport.transport_system.model.DriverUser;
import com.transport.transport_system.model.RegularUser;
import com.transport.transport_system.model.User;

/**
 * Factory Pattern Implementation for User Creation
 * Handles PASSENGER, DRIVER, CONDUCTOR, and ADMIN roles.
 */
public class UserFactory {

    public enum UserType {
        ADMIN("ADMIN"),
        PASSENGER("PASSENGER"),
        DRIVER("DRIVER"),
        CONDUCTOR("CONDUCTOR"),
        // kept for backward compat — treated same as PASSENGER
        REGULAR_USER("USER");

        private final String value;

        UserType(String value) { this.value = value; }

        public String getValue() { return value; }

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
     * Create a user based on role string from the registration form.
     * Accepts: PASSENGER, DRIVER, CONDUCTOR, ADMIN, USER
     */
    public static User createUser(String userType, String email, String password,
                                  String firstName, String lastName, String phoneNumber) {
        switch (userType.toUpperCase()) {
            case "ADMIN":
                return new Admin(email, password, firstName, lastName, phoneNumber);
            case "DRIVER":
                return new DriverUser(email, password, firstName, lastName, phoneNumber);
            case "CONDUCTOR":
                return new ConductorUser(email, password, firstName, lastName, phoneNumber);
            case "PASSENGER":
            case "USER":
            default:
                return new RegularUser(email, password, firstName, lastName, phoneNumber);
        }
    }

    public static Admin createAdmin(String email, String password, String firstName,
                                    String lastName, String phoneNumber,
                                    String department, String permissions) {
        return new Admin(email, password, firstName, lastName, phoneNumber, department, permissions);
    }

    public static RegularUser createRegularUser(String email, String password, String firstName,
                                                String lastName, String phoneNumber,
                                                String address, String city, String zipCode) {
        return new RegularUser(email, password, firstName, lastName, phoneNumber, address, city, zipCode);
    }

    public static DriverUser createDriverUser(String email, String password, String firstName,
                                              String lastName, String phoneNumber,
                                              String licenseNumber) {
        return new DriverUser(email, password, firstName, lastName, phoneNumber, licenseNumber);
    }
}