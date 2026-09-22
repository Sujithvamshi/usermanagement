package com.example.usermanagement.application.usecase;

/**
 * Input command for updating a user's editable fields.
 */
public record UpdateUserCommand(Long id, String fullName, String email) {
}
