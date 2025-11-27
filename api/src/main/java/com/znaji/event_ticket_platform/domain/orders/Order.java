package com.znaji.event_ticket_platform.domain.orders;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "attendee_id", nullable = false)
    private UUID attendeeId;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<OrderItem> items = new ArrayList<>();

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }


    //business rules:
    public static Order createOrder(UUID attendeeId,
                            UUID eventId,
                            List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order newOrder = new Order();
        newOrder.setEventId(eventId);
        newOrder.setAttendeeId(attendeeId);
        newOrder.setStatus(OrderStatus.PENDING);

        for (OrderItem item : items) {
            newOrder.addItem(item, newOrder);
        }

        return newOrder;
    }

    public void addItem(OrderItem item, Order order) {
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException(String.format("Quantity for item {%s} must be > 0", item.getId()));
        }

        if (item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("Unit price for item {%s} must be >= 0", item.getId()));
        }

        BigDecimal subTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));

        if (!subTotal.equals(item.getSubTotal())) {
            throw new IllegalStateException("Item subtotal mismatch");
        }

        item.setOrder(order);
        order.items.add(item);
        order.setTotalAmount(order.getTotalAmount().add(subTotal));
    }

    public void confirm() {
        if (this.status != OrderStatus.PAID) {
            throw new IllegalStateException("Order cannot be paid from status: " + this.status);
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only orders in pending payment status can be cancelled"
            );
        }

        this.status = OrderStatus.CANCELLED;
    }
}
