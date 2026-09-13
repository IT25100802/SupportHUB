package com.customersupport.SupportHUB.feedback;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FeedbackSpecification {

    public static Specification<Feedback> filterFeedback(Integer rating, String keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (rating != null) {
                predicates.add(criteriaBuilder.equal(root.get("rating"), rating));
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword.trim().toLowerCase() + "%";
                Predicate commentMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("comment")), kw);
                Predicate suggMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("suggestions")), kw);
                Predicate ticketMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("ticket").get("ticketNumber")), kw);
                predicates.add(criteriaBuilder.or(commentMatch, suggMatch, ticketMatch));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
