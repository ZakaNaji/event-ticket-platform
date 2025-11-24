package com.znaji.event_ticket_platform.application.events;

import com.znaji.event_ticket_platform.domain.events.Event;
import com.znaji.event_ticket_platform.domain.events.TicketType;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EventApplicationService {
    private final EventRepository eventRepository;

    public EventApplicationService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public UUID createEvent(CreateEventCommand cmd) {
        Event event = EventMapper.toEntity(cmd);
        eventRepository.save(event);
        return event.getId();
    }

    public void addTicketType(AddTicketTypeCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        TicketType type = EventMapper.toEntity(cmd, event);

        event.addTicketType(type);
        eventRepository.save(event);
    }

    private Event loadEvent(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
    }
}
