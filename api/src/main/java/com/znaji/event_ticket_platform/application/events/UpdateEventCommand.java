package com.znaji.event_ticket_platform.application.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateEventCommand(
        UUID eventId,
        String name,
        String description,
        LocalDateTime start,
        LocalDateTime end,
        String venue
) {
}
