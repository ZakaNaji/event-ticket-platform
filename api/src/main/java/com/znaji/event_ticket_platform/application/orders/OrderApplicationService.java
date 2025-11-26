package com.znaji.event_ticket_platform.application.orders;

import com.znaji.event_ticket_platform.api.orders.io.OrderResponse;
import com.znaji.event_ticket_platform.application.orders.commands.CreateOrderCommand;
import com.znaji.event_ticket_platform.application.orders.commands.CreateOrderItemCommand;
import com.znaji.event_ticket_platform.common.mapping.orders.OrderMapper;
import com.znaji.event_ticket_platform.domain.events.Event;
import com.znaji.event_ticket_platform.domain.events.EventStatus;
import com.znaji.event_ticket_platform.domain.events.TicketType;
import com.znaji.event_ticket_platform.domain.orders.Order;
import com.znaji.event_ticket_platform.domain.orders.OrderItem;
import com.znaji.event_ticket_platform.domain.orders.OrderStatus;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.EventRepository;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.TicketTypeRepository;
import com.znaji.event_ticket_platform.infrastructure.persistence.orders.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderApplicationService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final OrderRepository orderRepository;

    public OrderApplicationService(EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, OrderRepository orderRepository) {
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(CreateOrderCommand cmd) {
        Event event = loadEvent(cmd.eventId());

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new IllegalStateException("Can't order an event that isn't published");
        }

        if (event.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Can't order an event that already started");
        }

        List<OrderItem> items = createItems(cmd.items(), event);

        Order order = Order.createOrder(cmd.attendeeId(), cmd.eventId(), items);
        orderRepository.save(order);
        return OrderMapper.toResponse(order);
    }

    private List<OrderItem> createItems(List<CreateOrderItemCommand> itemsCommands, Event event) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateOrderItemCommand item : itemsCommands) {
            TicketType ticketType = loadTicketType(item.ticketTypeId());

            if (!ticketType.getEvent().getId().equals(event.getId())) {
                throw new IllegalStateException("Ticket type does not belong to the event");
            }
            checkAvailability(ticketType, item.quantity());

            orderItems.add(OrderItem.createItem(item.ticketTypeId(),
                    item.quantity(),
                    ticketType.getPrice(),
                    ticketType.getName()));
        }
        return orderItems;
    }

    public void confirmOrder(UUID orderId) {
        Order order = loadOrder(orderId);
        Event event = loadEvent(order.getEventId());

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new IllegalStateException("");
        }

        if (event.getStart().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot confirm order: event has already started");
        }

        for (OrderItem item : order.getItems()) {
            TicketType ticketType = loadTicketType(item.getTicketTypeId());

            if (!ticketType.getEvent().getId().equals(event.getId())) {
                throw new IllegalStateException("Ticket type does not belong to this event");
            }

            ticketType.increaseSoldQuantity(item.getQuantity());//internaly checks availability
        }
        order.confirm();
    }

    private Order loadOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order not found: %s", orderId)));
    }

    private void checkAvailability(TicketType ticketType, int quantity) {
        if (ticketType.getSoldQuantity() + quantity > ticketType.getMaxQuantity()) {
            throw new IllegalStateException("selected Quantity exceeds available %d ticket for type %s"
                    .formatted(ticketType.getMaxQuantity(), ticketType.getName()));
        }
    }

    private TicketType loadTicketType(UUID ticketTypeId) {
        return ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Ticket type not found: %s", ticketTypeId)));
    }

    private Event loadEvent(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Event not found: %s", id)));
    }

}
