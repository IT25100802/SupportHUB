package com.customersupport.SupportHUB.customer;

import com.customersupport.SupportHUB.common.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByUser(User user);
    Optional<Customer> findByUserId(Long userId);
    Optional<Customer> findByUserEmail(String email);
}
