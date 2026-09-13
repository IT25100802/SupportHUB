package com.customersupport.SupportHUB.agent;

import java.util.List;

public class UpdateAgentDetailsRequest {
    private String fullName;
    private String phone;
    private String role;
    private Boolean active;
    private AgentStatus status;
    private List<Long> categoryIds;

    public UpdateAgentDetailsRequest() {}

    public UpdateAgentDetailsRequest(String fullName, String phone, String role, Boolean active, AgentStatus status, List<Long> categoryIds) {
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.active = active;
        this.status = status;
        this.categoryIds = categoryIds;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
