package com.znaji.event_ticket_platform.common.mapping.orders;

import com.znaji.event_ticket_platform.api.orders.io.CreateOrderRequest;
import com.znaji.event_ticket_platform.api.orders.io.OrderItemResponse;
import com.znaji.event_ticket_platform.api.orders.io.OrderResponse;
import com.znaji.event_ticket_platform.application.orders.commands.CreateOrderCommand;
import com.znaji.event_ticket_platform.application.orders.commands.CreateOrderItemCommand;
import com.znaji.event_ticket_platform.domain.orders.Order;
import com.znaji.event_ticket_platform.domain.orders.OrderItem;

import java.util.List;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getAttendeeId(),
                order.getEventId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                itemResponses
        );
    }

    private static OrderItemResponse toResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getTicketTypeId(),
                item.getTicketTypeName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubTotal()
        );
    }

    public static CreateOrderCommand toCommand(CreateOrderRequest request) {

        List<CreateOrderItemCommand> itemCommands = request.items().stream()
                .map(item -> new CreateOrderItemCommand(
                        item.ticketTypeId(),
                        item.quantity()
                ))
                .toList();

        return new CreateOrderCommand(
                request.attendeeId(),
                request.eventId(),
                itemCommands
        );
    }
}
