package com.znaji.event_ticket_platform.domain;

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

    @Column(name = "organizer", nullable = false)
    private UUID organizer;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private List<TicketType> ticketTypes = new ArrayList<>();

    public List<TicketType> getTicketTypes() {
        return List.copyOf(ticketTypes);
    }

    public void addTicketType(TicketType ticketType) {
        ticketTypes.add(ticketType);
        ticketType.setEvent(this);
    }

    public void removeEventType(TicketType type) {
        ticketTypes.remove(type);
        type.setEvent(null);
    }

    
}
