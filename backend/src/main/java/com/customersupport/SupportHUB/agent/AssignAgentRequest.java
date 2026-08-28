package com.customersupport.SupportHUB.agent;

import jakarta.validation.constraints.NotNull;

public class AssignAgentRequest {

    @NotNull(message = "Agent ID is required")
    private Long agentId;

    public AssignAgentRequest() {
    }

    public AssignAgentRequest(Long agentId) {
        this.agentId = agentId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
}

