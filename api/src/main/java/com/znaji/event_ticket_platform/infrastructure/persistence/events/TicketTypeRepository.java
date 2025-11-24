package com.znaji.event_ticket_platform.infrastructure.persistence.events;

import com.znaji.event_ticket_platform.domain.events.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {
    List<TicketType> findAllByEventId(UUID eventId);
}
