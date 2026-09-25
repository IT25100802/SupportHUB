package com.customersupport.SupportHUB.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role = com.customersupport.SupportHUB.common.Role.CUSTOMER AND u.active = true AND " +
           "((u.lastLoginAt IS NOT NULL AND u.lastLoginAt < :threshold) OR " +
           "(u.lastLoginAt IS NULL AND u.createdAt < :threshold))")
    List<User> findInactiveCustomers(@Param("threshold") LocalDateTime threshold);
}
