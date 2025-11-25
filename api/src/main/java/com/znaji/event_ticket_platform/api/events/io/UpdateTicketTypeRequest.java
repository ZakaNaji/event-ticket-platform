package com.znaji.event_ticket_platform.api.events.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateTicketTypeRequest(
        @NotBlank String name,
        @NotNull BigDecimal price,
        @NotNull int maxQuantity
) {
}
