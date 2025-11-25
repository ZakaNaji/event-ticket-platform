package com.znaji.event_ticket_platform.domain.events;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "organizer_id", nullable = false)
    private UUID organizerId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @Column(nullable = false, length = 250)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    @OrderBy("price ASC")
    private List<TicketType> ticketTypes = new ArrayList<>();

    public List<TicketType> getTicketTypes() {
        return List.copyOf(ticketTypes);
    }


    //lifecycle methods:
    public void publish() {
        if (ticketTypes.isEmpty()) {
            throw new IllegalStateException("An event must have at least one ticket type to be published");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot publish an event that already started");
        }

        if (!end.isAfter(start)) {
            throw new IllegalStateException("Event end time must be after start time");
        }

        if (status != EventStatus.DRAFT) {
            throw new IllegalStateException("Only events in DRAFT state can be published");
        }
        this.status = EventStatus.PUBLISHED;
    }

    public void close() {
        if (status != EventStatus.PUBLISHED) {
            throw new IllegalStateException("Only Published events can be closed");
        }

        if (end.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot close event before it finishes");
        }

        this.status = EventStatus.CLOSED;
    }

    public void cancel() {
        if (status == EventStatus.CLOSED || status == EventStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot cancel an event that already completed");
        }
        this.status = EventStatus.CANCELLED;
    }

    public void archive() {
        if (status != EventStatus.CLOSED) {
            throw new IllegalStateException("Only closed events can be archived");
        }
        this.status = EventStatus.ARCHIVED;
    }

    public boolean isEditable() {
        return this.status == EventStatus.DRAFT;
    }

    public void updateDetails(String name, String description, LocalDateTime start, LocalDateTime end, String venue) {

        if (!isEditable()) {
            throw new IllegalStateException("Event can be updated only in DRAFT state");
        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Start date cannot be in the past");
        }

        if (!end.isAfter(start)) {
            throw new IllegalStateException("Event end time must be after the start");
        }

        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.venue = venue;
    }

    // TicketType rules inside the Event aggregate

    public void addTicketType(TicketType ticketType) {
        if (!isEditable()) {
            throw new IllegalStateException("Cannot add ticket types unless event is in DRAFT");
        }

        if (ticketType.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (ticketType.getMaxQuantity() <= 0) {
            throw new IllegalArgumentException("Max quantity must be > 0");
        }

        ticketTypes.add(ticketType);
        ticketType.setEvent(this);
    }

    public void updateTicketType(
            TicketType ticketType,
            String newName,
            BigDecimal newPrice,
            int newMaxQuantity
    ) {
        if (!isEditable()) {
            throw new IllegalStateException("Cannot modify ticket types after publishing");
        }

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (newMaxQuantity <= 0) {
            throw new IllegalArgumentException("Max quantity must be > 0");
        }

        if (newMaxQuantity < ticketType.getSoldQuantity()) {
            throw new IllegalArgumentException(
                    "Cannot reduce max quantity below already sold quantity"
            );
        }

        ticketType.updateName(newName);
        ticketType.updatePrice(newPrice);
        ticketType.updateMaxQuantity(newMaxQuantity);
    }

    public void removeTicketType(TicketType type) {
        if (!isEditable()) {
            throw new IllegalStateException("Cannot remove ticket types after publishing");
        }

        if (!type.canDelete()) {
            throw new IllegalStateException(
                    "Cannot delete a ticket type that has already sold tickets"
            );
        }

        ticketTypes.remove(type);
        type.setEvent(null);
    }
}
