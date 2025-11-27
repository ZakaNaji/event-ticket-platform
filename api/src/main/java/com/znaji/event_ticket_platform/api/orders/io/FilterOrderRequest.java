package com.znaji.event_ticket_platform.api.orders.io;

import com.znaji.event_ticket_platform.domain.orders.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record FilterOrderRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime fromDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime toDate,
        UUID eventId,
        OrderStatus status
) {}
