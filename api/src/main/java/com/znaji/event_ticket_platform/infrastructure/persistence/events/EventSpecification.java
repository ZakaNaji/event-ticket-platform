package com.znaji.event_ticket_platform.infrastructure.persistence.events;

import com.znaji.event_ticket_platform.api.events.EventFilterCommand;
import com.znaji.event_ticket_platform.domain.events.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> apply(EventFilterCommand cmd) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cmd.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), cmd.status()));
            }

            if (cmd.fromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("start"), cmd.fromDate()));
            }

            if (cmd.toDate() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("end"), cmd.toDate()));

            if (cmd.organizerId() != null)
                predicates.add(criteriaBuilder.equal(root.get("organizerId"), cmd.organizerId()));

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
