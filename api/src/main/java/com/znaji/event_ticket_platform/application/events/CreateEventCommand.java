package com.znaji.event_ticket_platform.application.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateEventCommand(UUID organizerId,
                                 String name,
                                 String description,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 String venue) {
}
