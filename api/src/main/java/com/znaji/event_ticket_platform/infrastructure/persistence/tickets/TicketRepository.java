package com.znaji.event_ticket_platform.infrastructure.persistence.tickets;

import com.znaji.event_ticket_platform.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
