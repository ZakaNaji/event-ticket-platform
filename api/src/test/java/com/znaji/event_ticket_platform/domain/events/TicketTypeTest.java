package com.znaji.event_ticket_platform.domain.events;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketTypeTest {
    @Test
    void increaseSoldQuantity_shouldWork() {
        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0, null);

        type.increaseSoldQuantity(10);

        assertEquals(10, type.getSoldQuantity());
    }

    @Test
    void increaseSoldQuantity_shouldFail_whenExceedsMax() {
        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 10, 5, null);

        assertThrows(IllegalStateException.class, () -> type.increaseSoldQuantity(20));
    }

    @Test
    void canDelete_shouldReturnFalse_whenSoldQuantityGreaterZero() {
        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 10, 5, null);

        assertFalse(type.canDelete());
    }

    @Test
    void canDelete_shouldReturnTrue_whenNoSales() {
        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 10, 0, null);

        assertTrue(type.canDelete());
    }

}