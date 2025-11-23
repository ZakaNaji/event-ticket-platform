package com.znaji.event_ticket_platform.domain.events;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ticket_types")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, name = "max_quantity")
    private int maxQuantity;

    @Column(nullable = false, name = "sold_quantity")
    private int soldQuantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Ticket type name cannot be empty");
        }
        this.name = name;
    }

    public void updatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public void updateMaxQuantity(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("Max quantity must be > 0");
        }
        this.maxQuantity = max;
    }

    public void increaseSoldQuantity(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        if (this.soldQuantity + qty > maxQuantity) {
            throw new IllegalStateException("Not enough tickets available");
        }

        this.soldQuantity += qty;
    }

    public boolean canDelete() {
        return this.soldQuantity == 0;
    }
}
