package com.znaji.event_ticket_platform.infrastructure.persistence.tickets;

import com.znaji.event_ticket_platform.domain.tickets.CheckInLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheckInLogRepository extends JpaRepository<CheckInLog, UUID> {
}
