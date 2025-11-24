package com.znaji.event_ticket_platform.infrastructure.persistence.events;

import com.znaji.event_ticket_platform.api.events.EventFilterCommand;
import com.znaji.event_ticket_platform.domain.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {
    Optional<Event> findById(UUID id);
}
