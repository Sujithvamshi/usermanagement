package com.example.usermanagement.application.exception;

/**
 * Thrown when a requested user cannot be found. Mapped to HTTP 404 by the
 * global exception handler.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
