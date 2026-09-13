package com.customersupport.SupportHUB.agent;

import com.customersupport.SupportHUB.category.TicketCategoryDto;

import java.time.LocalDateTime;
import java.util.Set;

public class SupportAgentDto {

    private Long id;
    private Long userId;
    private String email;
    private String fullName;
    private String phone;
    private String employeeCode;
    private AgentStatus status;
    private Set<TicketCategoryDto> assignedCategories;
    private long activeTicketCount;
    private LocalDateTime createdAt;

    public SupportAgentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public Set<TicketCategoryDto> getAssignedCategories() {
        return assignedCategories;
    }

    public void setAssignedCategories(Set<TicketCategoryDto> assignedCategories) {
        this.assignedCategories = assignedCategories;
    }

    public long getActiveTicketCount() {
        return activeTicketCount;
    }

    public void setActiveTicketCount(long activeTicketCount) {
        this.activeTicketCount = activeTicketCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

