package com.customersupport.SupportHUB.customer;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> searchCustomers(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String kw = "%" + keyword.trim().toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), kw));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("email")), kw));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), kw));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
