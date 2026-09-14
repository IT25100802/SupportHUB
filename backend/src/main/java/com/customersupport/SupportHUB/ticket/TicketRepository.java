package com.customersupport.SupportHUB.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    boolean existsByTicketNumber(String ticketNumber);

    List<Ticket> findByCustomerId(Long customerId);
    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);

    List<Ticket> findByAssignedAgentId(Long agentId);
    Page<Ticket> findByAssignedAgentId(Long agentId, Pageable pageable);

    List<Ticket> findByCategoryId(Long categoryId);

    long countByStatus(TicketStatus status);
    long countByPriority(TicketPriority priority);
    long countByCategoryId(Long categoryId);
    long countByAssignedAgentIdAndStatusNot(Long agentId, TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedAgent.id = :agentId AND t.status IN ('OPEN', 'IN_PROGRESS', 'WAITING_FOR_CUSTOMER')")
    long countActiveTicketsForAgent(@Param("agentId") Long agentId);

    @Query("SELECT t.status, COUNT(t) FROM Ticket t GROUP BY t.status")
    List<Object[]> countTicketsGroupedByStatus();

    @Query("SELECT t.priority, COUNT(t) FROM Ticket t GROUP BY t.priority")
    List<Object[]> countTicketsGroupedByPriority();

    @Query("SELECT t.category.name, COUNT(t) FROM Ticket t GROUP BY t.category.name")
    List<Object[]> countTicketsGroupedByCategory();

    @Query("SELECT t FROM Ticket t WHERE t.createdAt >= :startDate")
    List<Ticket> findRecentTickets(@Param("startDate") LocalDateTime startDate);
}
