package com.znaji.event_ticket_platform.application.events.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record AddTicketTypeCommand(
        UUID eventId,
        String name,
        BigDecimal price,
        int maxQuantity
) {
}
