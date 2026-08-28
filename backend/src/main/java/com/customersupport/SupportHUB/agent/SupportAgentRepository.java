package com.customersupport.SupportHUB.agent;

import com.customersupport.SupportHUB.common.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupportAgentRepository extends JpaRepository<SupportAgent, Long> {
    Optional<SupportAgent> findByUser(User user);
    Optional<SupportAgent> findByUserId(Long userId);
    Optional<SupportAgent> findByEmployeeCode(String employeeCode);
    boolean existsByEmployeeCode(String employeeCode);
    List<SupportAgent> findByStatus(AgentStatus status);

    @Query("SELECT sa FROM SupportAgent sa JOIN sa.assignedCategories c WHERE c.id = :categoryId AND sa.user.active = true")
    List<SupportAgent> findByCategoryId(@Param("categoryId") Long categoryId);
}

