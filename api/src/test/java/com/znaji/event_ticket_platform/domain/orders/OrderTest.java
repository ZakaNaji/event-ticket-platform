package com.znaji.event_ticket_platform.domain.orders;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    // Utility data creator
    private OrderItem item(UUID ticketTypeId, int quantity, BigDecimal price) {
        return OrderItem.createItem(ticketTypeId, quantity, price, "Some Ticket");
    }

    @Test
    void createOrderWithValidItemsShouldSucceed() {

        UUID attendeeId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        OrderItem item = item(UUID.randomUUID(), 2, new BigDecimal("100.00"));

        Order order = Order.createOrder(
                attendeeId,
                eventId,
                List.of(item)
        );

        assertEquals(order.getStatus(), OrderStatus.PENDING);
        assertEquals(order.getTotalAmount(), new BigDecimal("200.00"));
        assertEquals(order.getItems().size(), 1);
    }

    @Test
    void createOrderFailsIfItemsEmpty() {
        UUID attendeeId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> Order.createOrder(attendeeId, eventId, List.of()));
    }


    @Test
    void confirmOrder_successful() {
        Order order = Order.createOrder(
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(item(UUID.randomUUID(), 1, new BigDecimal("50.00")))
        );

        order.confirm();

        assertEquals(order.getStatus(),OrderStatus.PAID);
    }

}