package com.znaji.event_ticket_platform.domain.tickets;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "check_in_log")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CheckInLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId; //OR SHOULD THIS BE AN EMBEDDED OBJECT?

    @Column(name = "stuff_id", nullable = false)
    private UUID stuffId;

    @Column(nullable = false)
    private LocalDateTime scanTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScanResult result;
}
