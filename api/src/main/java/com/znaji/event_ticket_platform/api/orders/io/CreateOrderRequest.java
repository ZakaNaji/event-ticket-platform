package com.znaji.event_ticket_platform.api.orders.io;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull(message = "Attendee ID is required")
        UUID attendeeId,

        @NotNull(message = "Event ID is required")
        UUID eventId,

        @NotEmpty(message = "Order items cannot be empty")
        List<@Valid CreateOrderItemRequest> items
) {
}
