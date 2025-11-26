package com.znaji.event_ticket_platform.application.orders.commands;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        UUID attendeeId,
        UUID eventId,
        List<CreateOrderItemCommand> items
        ) {
}
