package com.znaji.event_ticket_platform.domain.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;

    @BeforeEach
    public void createEvent() {
        this.event = new Event();
        this.event.setId(UUID.randomUUID());
        this.event.setOrganizerId(UUID.randomUUID());
        this.event.setName("Test Event");
        this.event.setStart(LocalDateTime.now().plusDays(5));
        this.event.setEnd(LocalDateTime.now().plusDays(6));
        this.event.setVenue("Test Venue");
        this.event.setStatus(EventStatus.DRAFT);
    }

    @Test
    void publish_shouldWork_whenDraftAndHasTicketTypes() {
        event.addTicketType(new TicketType(UUID.randomUUID(),
                "VIP",
                BigDecimal.TEN,
                100,
                0,
                event));

        event.publish();

        assertEquals(event.getStatus(), EventStatus.PUBLISHED);
    }

    @Test
    void publish_shouldFail_whenNoTicketTypes(){

        assertThrows(IllegalStateException.class, () -> event.publish());
    }

    @Test
    void publish_shouldFail_whenStartDatePast() {
        event.setStart(LocalDateTime.now().minusDays(2));

        assertThrows(IllegalStateException.class,
                event::publish);
    }

    @Test
    void close_shouldWork_whenPublishedAndEventEnded() {
        event.addTicketType(new TicketType(UUID.randomUUID(),
                "VIP",
                BigDecimal.TEN,
                100,
                0,
                event));

        event.publish();

        event.setEnd(LocalDateTime.now());

        event.close();

        assertEquals(EventStatus.CLOSED, event.getStatus());
    }

    @Test
    void close_shouldFail_whenEventNotFinished() {

        event.addTicketType(new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0, event));
        event.publish();

        event.setEnd(LocalDateTime.now().plusDays(1));

        assertThrows(IllegalStateException.class, event::close);
    }

    @Test
    void addTicketType_shouldFail_whenNotDraft() {
        event.setStatus(EventStatus.PUBLISHED);

        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0, event);

        assertThrows(IllegalStateException.class, () -> event.addTicketType(type));
    }

    @Test
    void updateTicketType_shouldFail_whenNotEditable() {
        TicketType type = new TicketType(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0, event);

        event.addTicketType(type);
        event.publish();

        assertThrows(IllegalStateException.class,
                () -> event.updateTicketType(type, "NewName", BigDecimal.ONE, 10));
    }
}