package com.laoumri.socialmediaspringbackend.exceptions;

public class EmailAddressAlreadyExists extends RuntimeException {
    public EmailAddressAlreadyExists() {
        super("Email Address Already Exists. Login instead.");
    }
}
