package com.customersupport.SupportHUB.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    Optional<TicketCategory> findByName(String name);
    boolean existsByName(String name);
    List<TicketCategory> findByActiveTrue();
    List<TicketCategory> findByParentIsNull();
    List<TicketCategory> findByParentIsNullAndActiveTrue();
    List<TicketCategory> findByParentId(Long parentId);
    List<TicketCategory> findByParentIdAndActiveTrue(Long parentId);
    long countByParentId(Long parentId);
}
