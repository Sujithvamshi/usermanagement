package com.example.usermanagement.web.dto;

import com.example.usermanagement.domain.User;

import java.time.Instant;

/**
 * API response representation of a user. Deliberately separate from the JPA
 * entity and the domain model so persistence details never leak through the
 * API.
 */
public record UserResponse(Long id, String fullName, String email, Instant createdAt, Instant updatedAt) {

    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
