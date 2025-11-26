package com.znaji.event_ticket_platform.application.orders.commands;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateOrderItemCommand(
        UUID ticketTypeId,
        int quantity
        ) {
}
