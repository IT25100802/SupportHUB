package com.customersupport.SupportHUB.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CreateCustomerRequest request);
    CustomerDto getCustomerById(Long id);
    CustomerDto getCustomerByUserId(Long userId);
    CustomerDto getCustomerByEmail(String email);
    List<CustomerDto> getAllCustomers();
    Page<CustomerDto> searchCustomers(String keyword, Pageable pageable);
    CustomerDto updateCustomer(Long id, UpdateCustomerRequest request);
    void deleteCustomer(Long id);
}

