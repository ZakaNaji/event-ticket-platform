package com.znaji.event_ticket_platform.infrastructure.persistence.events;

import com.znaji.event_ticket_platform.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findById(UUID id);
}
