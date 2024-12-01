package com.laoumri.socialmediaspringbackend.exceptions;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
