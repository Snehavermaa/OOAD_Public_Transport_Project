package com.transport.transport_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CONDUCTOR")
public class ConductorUser extends User {

    public ConductorUser() {
        super();
        this.setRole("CONDUCTOR");
    }

    public ConductorUser(String email, String password, String firstName, String lastName, String phoneNumber) {
        super(email, password, firstName, lastName, phoneNumber, "CONDUCTOR");
    }

    @Override
    public String getUserType() {
        return "CONDUCTOR";
    }
}
