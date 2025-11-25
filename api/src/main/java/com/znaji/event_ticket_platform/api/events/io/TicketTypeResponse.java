package com.znaji.event_ticket_platform.api.events.io;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeResponse(
        UUID id,
        String name,
        BigDecimal price,
        int maxQuantity,
        int soldQuantity
) {
}
