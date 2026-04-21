package com.transport.transport_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("USER")
public class RegularUser extends User {

    private String address;
    private String city;
    private String zipCode;

    // Constructors
    public RegularUser() {
        super();
        this.setRole("PASSENGER");
    }

    public RegularUser(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber, "PASSENGER");
    }

    public RegularUser(String email, String password, String firstName, String lastName, String phoneNumber,
                       String address, String city, String zipCode) {
        super(email, password, firstName, lastName, phoneNumber, "PASSENGER");
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
    }

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String getUserType() {
        return "USER";
    }

    @Override
    public String toString() {
        return "RegularUser{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
