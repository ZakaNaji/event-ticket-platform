package com.znaji.event_ticket_platform.application.events;

import com.znaji.event_ticket_platform.common.mapping.events.EventMapper;
import com.znaji.event_ticket_platform.domain.events.Event;
import com.znaji.event_ticket_platform.domain.events.TicketType;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.EventRepository;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.TicketTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class EventApplicationService {
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public EventApplicationService(EventRepository eventRepository, TicketTypeRepository ticketTypeRepository) {
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
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

    public void updateTicketType(UpdateTicketTypeCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        TicketType ticketType = findTicketType(cmd.ticketTypeId(), event);

        event.updateTicketType(
                ticketType,
                cmd.name(),
                cmd.price(),
                cmd.maxQuantity()
        );
        eventRepository.save(event);
    }

    public void removeTicketType(UUID eventId, UUID ticketTypeId) {
        Event event = loadEvent(eventId);
        TicketType ticketType = findTicketType(ticketTypeId, event);

        event.removeTicketType(ticketType);

        eventRepository.save(event);
    }

    public void publish(EventIdCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        event.publish();
        eventRepository.save(event);
    }

    public void close(EventIdCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        event.close();
        eventRepository.save(event);
    }

    public void cancel(EventIdCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        event.close();
        eventRepository.save(event);
    }

    public void archive(EventIdCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        event.archive();
        eventRepository.save(event);
    }

    private TicketType findTicketType(UUID typeId, Event event) {
        return event.getTicketTypes().stream()
                .filter(type -> type.getId().equals(typeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("TicketType not found: " + typeId));
    }

    private Event loadEvent(UUID id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
    }
}
