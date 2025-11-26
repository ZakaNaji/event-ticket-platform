package com.znaji.event_ticket_platform.api.orders.io;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderItemRequest(
        @NotNull(message = "Ticket type ID is required")
        UUID ticketTypeId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) { }
