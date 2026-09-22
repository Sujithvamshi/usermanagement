package com.example.usermanagement.application.usecase;

/**
 * Input command for creating a user. Kept in the application layer so
 * business rules do not leak into the web layer.
 */
public record CreateUserCommand(String fullName, String email) {
}
