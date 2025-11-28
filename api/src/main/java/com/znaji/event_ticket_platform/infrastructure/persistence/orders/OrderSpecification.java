package com.znaji.event_ticket_platform.infrastructure.persistence.orders;

import com.znaji.event_ticket_platform.application.orders.commands.FilterOrderCommand;
import com.znaji.event_ticket_platform.domain.orders.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> filterOrder(FilterOrderCommand command) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (command.eventId() != null) {
                 predicates.add(criteriaBuilder.equal(root.get("eventId"), command.eventId()));
            }

            if (command.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), command.status()));
            }

            if (command.fromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), command.fromDate()));
            }

            if (command.toDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), command.toDate()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
