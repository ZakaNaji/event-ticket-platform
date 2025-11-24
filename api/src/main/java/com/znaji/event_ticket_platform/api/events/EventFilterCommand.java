package com.znaji.event_ticket_platform.api.events;

import com.znaji.event_ticket_platform.domain.events.EventStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventFilterCommand(
        LocalDateTime fromDate,
        LocalDateTime toDate,
        EventStatus status,
        UUID organizerId
) {
}
