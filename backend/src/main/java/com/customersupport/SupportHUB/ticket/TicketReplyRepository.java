package com.customersupport.SupportHUB.ticket;

import com.customersupport.SupportHUB.common.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketReplyRepository extends JpaRepository<TicketReply, Long> {
    List<TicketReply> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
    List<TicketReply> findByTicketIdAndInternalFalseOrderByCreatedAtAsc(Long ticketId);
    void deleteByUser(User user);
}
