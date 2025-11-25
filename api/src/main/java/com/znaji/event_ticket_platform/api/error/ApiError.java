package com.znaji.event_ticket_platform.api.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiError(
        String message,
        int status,
        String path,
        LocalDateTime timestamp
) {
    public static ApiError of(
            String message,
            HttpStatus status,
            String path
    ) {
        return new ApiError(message, status.value(), path, LocalDateTime.now());
    }
}
