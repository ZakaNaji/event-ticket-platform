package com.znaji.event_ticket_platform.domain.events;

import jakarta.persistence.*;
import lombok.*;

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

    public void addTicketType(TicketType ticketType) {
        ticketTypes.add(ticketType);
        ticketType.setEvent(this);
    }

    public void removeTicketType(TicketType type) {
        ticketTypes.remove(type);
        type.setEvent(null);
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
    
}
