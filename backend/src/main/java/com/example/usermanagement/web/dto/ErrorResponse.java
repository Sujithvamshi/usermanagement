package com.example.usermanagement.web.dto;

import java.time.Instant;
import java.util.List;

/**
 * Consistent error envelope returned by the global exception handler.
 */
public record ErrorResponse(
        boolean success,
        String message,
        int status,
        Instant timestamp,
        List<String> details
) {

    public static ErrorResponse of(String message, int status) {
        return new ErrorResponse(false, message, status, Instant.now(), List.of());
    }

    public static ErrorResponse of(String message, int status, List<String> details) {
        return new ErrorResponse(false, message, status, Instant.now(), details);
    }
}
