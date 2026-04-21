package com.transport.transport_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DRIVER")
public class DriverUser extends User {

    private String licenseNumber;

    public DriverUser() {
        super();
        this.setRole("DRIVER");
    }

    public DriverUser(String email, String password, String firstName,
                      String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber, "DRIVER");
    }

    public DriverUser(String email, String password, String firstName,
                      String lastName, String phoneNumber, String licenseNumber) {
        super(email, password, firstName, lastName, phoneNumber, "DRIVER");
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    @Override
    public String getUserType() { return "DRIVER"; }
}