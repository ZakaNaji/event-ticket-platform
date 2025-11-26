package com.znaji.event_ticket_platform.api.orders.io;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID attendeeId,
        UUID eventId,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {}