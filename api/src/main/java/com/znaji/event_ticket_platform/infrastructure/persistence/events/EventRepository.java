package com.znaji.event_ticket_platform.infrastructure.persistence.events;

import com.znaji.event_ticket_platform.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
