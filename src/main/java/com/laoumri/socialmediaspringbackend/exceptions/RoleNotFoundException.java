package com.laoumri.socialmediaspringbackend.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super("Role " + role + " not found. The role name is misspelled or not available.");
    }
}