package com.znaji.event_ticket_platform.api.events.io;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventResponse(
        UUID id,
        UUID organizerId,
        String name,
        String description,
        LocalDateTime start,
        LocalDateTime end,
        String venue,
        String status,
        List<TicketTypeResponse> ticketTypes
) {
}
