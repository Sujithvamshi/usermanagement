package com.example.usermanagement.application.exception;

/**
 * Thrown when attempting to create or update a user with an email address
 * already in use. Mapped to HTTP 409 by the global exception handler.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("A user with email '" + email + "' already exists");
    }
}
