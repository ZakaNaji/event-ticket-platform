package com.znaji.event_ticket_platform.infrastructure.persistence.orders;

import com.znaji.event_ticket_platform.domain.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
