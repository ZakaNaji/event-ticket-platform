package com.znaji.event_ticket_platform.api.events;

import com.znaji.event_ticket_platform.application.events.*;
import com.znaji.event_ticket_platform.common.mapping.events.EventMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventApplicationService eventApplicationService;

    public EventsController(EventApplicationService eventApplicationService) {
        this.eventApplicationService = eventApplicationService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable UUID eventId) {
        EventResponse response = eventApplicationService.findEventById(eventId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}/ticket-types")
    public ResponseEntity<List<TicketTypeResponse>> getAllTypeForAnEvent(@PathVariable UUID eventId) {
        List<TicketTypeResponse> typeResponses = eventApplicationService.findTicketTypesForEvent(eventId);
        return ResponseEntity.ok(typeResponses);
    }

    @PostMapping()
    public ResponseEntity<UUID> createEvent(@Valid @RequestBody CreateEventRequest request) {
        CreateEventCommand createEventCommand = EventMapper.toCommand(request);
        UUID result = eventApplicationService.createEvent(createEventCommand);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Void> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequest request) {

        UpdateEventCommand eVentUpdateCommand = EventMapper.toEVentUpdateCommand(eventId, request);
        eventApplicationService.updateEvent(eVentUpdateCommand);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/ticket-types")
    public ResponseEntity<Void> addTicketType(@PathVariable("eventId") UUID eventId,
                                              @Valid @RequestBody AddTicketTypeRequest request) {
        AddTicketTypeCommand cmd = EventMapper.toCommand(eventId, request);
        eventApplicationService.addTicketType(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{eventId}/ticket-types/{ticketTypeId}")
    public ResponseEntity<Void> updateTicketType(@PathVariable("eventId") UUID eventId,
                                                 @PathVariable("ticketTypeId") UUID ticketTypeId,
                                                 @Valid @RequestBody UpdateTicketTypeRequest request) {

        UpdateTicketTypeCommand cmd = EventMapper.toCommand(eventId, ticketTypeId, request);
        eventApplicationService.updateTicketType(cmd);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{eventId}/ticket-types/{ticketTypeId}")
    public ResponseEntity<Void> deleteTicketType(
            @PathVariable UUID eventId,
            @PathVariable UUID ticketTypeId
    ) {
        eventApplicationService.removeTicketType(eventId, ticketTypeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{eventId}/publish")
    public ResponseEntity<Void> publishEvent(@PathVariable UUID eventId) {
        eventApplicationService.publish(new EventIdCommand(eventId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{eventId}/close")
    public ResponseEntity<Void> closeEvent(@PathVariable UUID eventId) {
        eventApplicationService.close(new EventIdCommand(eventId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{eventId}/cancel")
    public ResponseEntity<Void> cancelEvent(@PathVariable UUID eventId) {
        eventApplicationService.cancel(new EventIdCommand(eventId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{eventId}/archive")
    public ResponseEntity<Void> archiveEvent(@PathVariable UUID eventId) {
        eventApplicationService.archive(new EventIdCommand(eventId));
        return ResponseEntity.noContent().build();
    }
}
