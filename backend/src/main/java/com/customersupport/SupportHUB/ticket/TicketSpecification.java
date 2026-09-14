package com.customersupport.SupportHUB.ticket;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TicketSpecification {

    public static Specification<Ticket> filterTickets(
            Long customerId,
            Long agentId,
            Long categoryId,
            TicketStatus status,
            TicketPriority priority,
            String searchKeyword) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (customerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("customer").get("id"), customerId));
            }

            if (agentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignedAgent").get("id"), agentId));
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String kw = "%" + searchKeyword.trim().toLowerCase() + "%";
                Predicate numberMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("ticketNumber")), kw);
                Predicate subjectMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("subject")), kw);
                Predicate descMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), kw);
                predicates.add(criteriaBuilder.or(numberMatch, subjectMatch, descMatch));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
