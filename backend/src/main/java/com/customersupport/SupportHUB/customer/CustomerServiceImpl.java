package com.customersupport.SupportHUB.customer;

import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.common.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomers(String keyword, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecification.searchCustomers(keyword), pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer updated = customerRepository.save(customer);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        User user = customer.getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    private CustomerDto mapToDto(Customer c) {
        CustomerDto dto = new CustomerDto();
        dto.setId(c.getId());
        dto.setUserId(c.getUser().getId());
        dto.setEmail(c.getUser().getEmail());
        dto.setFullName(c.getFullName());
        dto.setPhone(c.getPhone());
        dto.setAddress(c.getAddress());
        dto.setActive(c.getUser().isActive());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }
}
