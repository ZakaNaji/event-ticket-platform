package com.znaji.event_ticket_platform.application.events;

import com.znaji.event_ticket_platform.api.events.EventFilterCommand;
import com.znaji.event_ticket_platform.api.events.EventResponse;
import com.znaji.event_ticket_platform.api.events.TicketTypeResponse;
import com.znaji.event_ticket_platform.common.mapping.events.EventMapper;
import com.znaji.event_ticket_platform.domain.events.Event;
import com.znaji.event_ticket_platform.domain.events.TicketType;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.EventRepository;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.EventSpecification;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.TicketTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        event.cancel();
        eventRepository.save(event);
    }

    public void archive(EventIdCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        event.archive();
        eventRepository.save(event);
    }
    public EventResponse findEventById(UUID eventId) {
        Event event = loadEvent(eventId);
        return EventMapper.toResponse(event);
    }

    public List<TicketTypeResponse> findTicketTypesForEvent(UUID eventId) {
        List<TicketType> allByEventId = ticketTypeRepository.findAllByEventId(eventId);

        return allByEventId.stream()
                .map(EventMapper::toTicketTypeResponse)
                .toList();
    }

    public void updateEvent(UpdateEventCommand cmd) {
        Event event = loadEvent(cmd.eventId());
        event.updateDetails(cmd.name(), cmd.description(), cmd.start(), cmd.end(), cmd.venue());
        eventRepository.save(event);
    }

    public Page<EventResponse> filterEvents(EventFilterCommand cmd, Pageable pageable) {
        if (cmd.fromDate() != null && cmd.toDate() != null) {
            if (cmd.fromDate().isAfter(cmd.toDate())) {
                throw new IllegalArgumentException("fromDate cannot be after toDate");
            }
        }

        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size too large");
        }
        Page<Event> filteredEvents = eventRepository.findAll(EventSpecification.apply(cmd), pageable);
        return filteredEvents.
                map(EventMapper::toResponse);
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
