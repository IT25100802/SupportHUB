package com.customersupport.SupportHUB.ticket;

import com.customersupport.SupportHUB.common.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistory> findByTicketIdOrderByTimestampDesc(Long ticketId);

    @Modifying
    @Query("UPDATE TicketHistory th SET th.performedBy = null WHERE th.performedBy = :user")
    void dissociateUser(@Param("user") User user);
}
