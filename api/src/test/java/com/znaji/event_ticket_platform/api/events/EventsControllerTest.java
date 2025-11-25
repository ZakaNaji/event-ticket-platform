package com.znaji.event_ticket_platform.api.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.znaji.event_ticket_platform.api.events.io.*;
import com.znaji.event_ticket_platform.application.events.EventApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventsController.class)
class EventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventApplicationService eventService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getEventById_shouldReturn200_andEventResponse() throws Exception {
        UUID id = UUID.randomUUID();

        EventResponse mockResponse = new EventResponse(
                id,
                UUID.randomUUID(),
                "Tech Conf",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Venue",
                "DRAFT",
                List.of()
        );
        when(eventService.findEventById(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/events/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Tech Conf"));

    }
    @Test
    void createEvent_shouldReturn201() throws Exception {

        CreateEventRequest request = new CreateEventRequest(
                UUID.randomUUID(),
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Venue"
        );

        when(eventService.createEvent(any())).thenReturn(UUID.randomUUID());

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void invalidCreateEvent_shouldReturn400() throws Exception {
        CreateEventRequest badReq = new CreateEventRequest(
                null,   // invalid
                "",
                null,
                null,
                null,
                ""
        );

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void filterEvents_shouldReturn200() throws Exception {
        Page<EventResponse> page = new PageImpl<>(List.of(
                new EventResponse(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "Event1",
                        "Desc",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        "Venue",
                        "DRAFT",
                        List.of()
                )
        ));

        when(eventService.filterEvents(any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getAllTypeForAnEvent_shouldReturnList() throws Exception {
        UUID eventId = UUID.randomUUID();

        when(eventService.findTicketTypesForEvent(any()))
                .thenReturn(List.of(
                        new TicketTypeResponse(UUID.randomUUID(), "VIP", BigDecimal.TEN, 100, 0)
                ));

        mockMvc.perform(get("/api/events/"+eventId+"/ticket-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name").value("VIP"));
    }

    @Test
    void updateEvent_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();

        UpdateEventRequest req = new UpdateEventRequest(
                "Updated Event",
                "Updated Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Updated Venue"
        );

        mockMvc.perform(put("/api/events/" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());
    }

    @Test
    void addTicketType_shouldReturn201() throws Exception {
        UUID eventId = UUID.randomUUID();

        AddTicketTypeRequest req = new AddTicketTypeRequest("VIP", BigDecimal.TEN, 100);

        mockMvc.perform(post("/api/events/" + eventId + "/ticket-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateTicketType_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        UpdateTicketTypeRequest req =
                new UpdateTicketTypeRequest("NewName", BigDecimal.valueOf(20), 150);

        mockMvc.perform(put("/api/events/" + eventId + "/ticket-types/" + typeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteTicketType_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();
        UUID typeId = UUID.randomUUID();

        mockMvc.perform(delete("/api/events/" + eventId + "/ticket-types/" + typeId))
                .andExpect(status().isNoContent());
    }

    @Test
    void publishEvent_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();

        mockMvc.perform(put("/api/events/" + eventId + "/publish"))
                .andExpect(status().isNoContent());
    }

    @Test
    void closeEvent_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();

        mockMvc.perform(put("/api/events/" + eventId + "/close"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelEvent_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();

        mockMvc.perform(put("/api/events/" + eventId + "/cancel"))
                .andExpect(status().isNoContent());
    }

    @Test
    void archiveEvent_shouldReturn204() throws Exception {
        UUID eventId = UUID.randomUUID();

        mockMvc.perform(put("/api/events/" + eventId + "/archive"))
                .andExpect(status().isNoContent());
    }

}