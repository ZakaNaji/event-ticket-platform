package com.znaji.event_ticket_platform.application.orders.commands;

import com.znaji.event_ticket_platform.domain.orders.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FilterOrderCommand(
        UUID eventId,
        OrderStatus status,
        LocalDateTime fromDate,
        LocalDateTime toDate
) {
}
