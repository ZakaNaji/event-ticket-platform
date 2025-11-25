package com.znaji.event_ticket_platform.application.events;

import com.znaji.event_ticket_platform.application.events.commands.CreateEventCommand;
import com.znaji.event_ticket_platform.application.events.commands.EventIdCommand;
import com.znaji.event_ticket_platform.application.events.commands.UpdateTicketTypeCommand;
import com.znaji.event_ticket_platform.domain.events.Event;
import com.znaji.event_ticket_platform.domain.events.EventStatus;
import com.znaji.event_ticket_platform.domain.events.TicketType;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.EventRepository;
import com.znaji.event_ticket_platform.infrastructure.persistence.events.TicketTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EventApplicationServiceTest {

    @Mock
    EventRepository eventRepository;

    @Mock
    TicketTypeRepository ticketTypeRepository;

    @InjectMocks
    EventApplicationService service;

    private Event draftEvent;

    @BeforeEach
    void setup() {
        draftEvent = new Event();
        draftEvent.setId(UUID.randomUUID());
        draftEvent.setOrganizerId(UUID.randomUUID());
        draftEvent.setName("Test Event");
        draftEvent.setStart(LocalDateTime.now().plusDays(5));
        draftEvent.setEnd(LocalDateTime.now().plusDays(6));
        draftEvent.setVenue("Some Venue");
        draftEvent.setStatus(EventStatus.DRAFT);
    }

    @Test
    void createEvent_shouldSaveAndReturnId() {
        CreateEventCommand cmd = new CreateEventCommand(
                draftEvent.getOrganizerId(),
                draftEvent.getName(),
                draftEvent.getDescription(),
                draftEvent.getStart(),
                draftEvent.getEnd(),
                draftEvent.getVenue()
        );

        when(eventRepository.save(any())).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            event.setId(UUID.randomUUID());
            return event;
        });
        UUID eventId = service.createEvent(cmd);

        assertNotNull(eventId);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void publishEvent_shouldWork() {

        when(eventRepository.findById(any())).thenReturn(Optional.of(draftEvent));

        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0, draftEvent);
        draftEvent.addTicketType(type);

        service.publish(new EventIdCommand(draftEvent.getId()));

        assertEquals(draftEvent.getStatus(), EventStatus.PUBLISHED);

        verify(eventRepository).save(draftEvent);
    }

    @Test
    void updateTicketType_shouldApplyDomainRules() {
        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0, draftEvent);
        draftEvent.addTicketType(type);

        when(eventRepository.findById(draftEvent.getId()))
                .thenReturn(Optional.of(draftEvent));

        UpdateTicketTypeCommand cmd = new UpdateTicketTypeCommand(
                draftEvent.getId(),
                type.getId(),
                "New VIP",
                BigDecimal.valueOf(20),
                200
        );

        service.updateTicketType(cmd);

        assertEquals("New VIP", type.getName());
        assertEquals(BigDecimal.valueOf(20), type.getPrice());
    }

}