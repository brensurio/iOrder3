package com.app.brensurio.iorder.models;

/**
 * Created by Mariz L. Maas on 10/17/2016.
 */

public class User {
    private String firstName;
    private String lastName;
    private String eid;
    private String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstName, String lastName, String eid, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eid = eid;
        this.email = email;
    }

    public String getEid() {
        return eid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() { return email;}
}
