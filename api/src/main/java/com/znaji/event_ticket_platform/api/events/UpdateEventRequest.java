package com.znaji.event_ticket_platform.api.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        @NotBlank String name,
        String description,
        @NotNull LocalDateTime start,
        @NotNull LocalDateTime end,
        @NotBlank String venue
) {
}
