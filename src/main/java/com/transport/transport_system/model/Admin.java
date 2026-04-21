package com.transport.transport_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    private String department;
    private String permissions;

    // Constructors
    public Admin() {
        super();
    }

    public Admin(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber, "ADMIN");
        this.department = "Management";
        this.permissions = "ALL_PERMISSIONS";
    }

    public Admin(String email, String password, String firstName, String lastName, String phoneNumber, 
                 String department, String permissions) {
        super(email, password, firstName, lastName, phoneNumber, "ADMIN");
        this.department = department;
        this.permissions = permissions;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    @Override
    public String getUserType() {
        return "ADMIN";
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", department='" + department + '\'' +
                ", permissions='" + permissions + '\'' +
                '}';
    }
}
