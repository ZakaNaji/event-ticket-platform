package com.znaji.event_ticket_platform.api.events.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventRequest(
        @NotNull UUID organizerId,
        @NotBlank String name,
        String description,
        @NotNull LocalDateTime start,
        @NotNull LocalDateTime end,
        @NotBlank String venue
) {
}
