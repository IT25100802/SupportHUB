package com.customersupport.SupportHUB.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
    Optional<Feedback> findByTicketId(Long ticketId);
    boolean existsByTicketId(Long ticketId);
    List<Feedback> findByCustomerId(Long customerId);

    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double calculateAverageRating();

    @Query("SELECT f.rating, COUNT(f) FROM Feedback f GROUP BY f.rating")
    List<Object[]> countFeedbackGroupedByRating();
}
