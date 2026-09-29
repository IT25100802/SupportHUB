package com.customersupport.SupportHUB.agent;

import java.util.List;

public interface AgentService {
    SupportAgentDto createAgent(CreateAgentRequest request);

    SupportAgentDto getAgentById(Long id);

    SupportAgentDto getAgentByUserId(Long userId);

    List<SupportAgentDto> getAllAgents();

    List<SupportAgentDto> getAgentsByCategory(Long categoryId);

    SupportAgentDto updateAgentCategories(Long agentId, UpdateAgentCategoriesRequest request);

    SupportAgentDto updateAgentDetails(Long agentId, UpdateAgentDetailsRequest request);

    SupportAgentDto updateAgentStatus(Long agentId, AgentStatus status);

    SupportAgentDto updateMyStatus(String email, AgentStatus status);

    void deleteAgent(Long agentId);
}
