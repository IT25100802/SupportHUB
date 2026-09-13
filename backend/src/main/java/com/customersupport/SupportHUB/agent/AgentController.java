package com.customersupport.SupportHUB.agent;

import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<SupportAgentDto>> createAgent(@Valid @RequestBody CreateAgentRequest request) {
        SupportAgentDto agent = agentService.createAgent(request);
        return ResponseEntity.ok(ApiResponse.success("Support agent created successfully", agent));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATIONS_SUPERVISOR', 'CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<SupportAgentDto>> getAgentById(@PathVariable("id") Long id) {
        SupportAgentDto agent = agentService.getAgentById(id);
        return ResponseEntity.ok(ApiResponse.success("Support agent fetched successfully", agent));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATIONS_SUPERVISOR', 'CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<List<SupportAgentDto>>> getAllAgents() {
        List<SupportAgentDto> agents = agentService.getAllAgents();
        return ResponseEntity.ok(ApiResponse.success("Support agents list fetched successfully", agents));
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('OPERATIONS_SUPERVISOR', 'CUSTOMER_SERVICE_OFFICER')")
    public ResponseEntity<ApiResponse<List<SupportAgentDto>>> getAgentsByCategory(@PathVariable("categoryId") Long categoryId) {
        List<SupportAgentDto> agents = agentService.getAgentsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Agents for category fetched successfully", agents));
    }

    @PutMapping("/{id}/categories")
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<SupportAgentDto>> updateAgentCategories(
            @PathVariable("id") Long id,
            @RequestBody UpdateAgentCategoriesRequest request) {
        SupportAgentDto updated = agentService.updateAgentCategories(id, request);
        return ResponseEntity.ok(ApiResponse.success("Agent categories updated successfully", updated));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.POST})
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<SupportAgentDto>> updateAgentDetails(
            @PathVariable("id") Long id,
            @RequestBody UpdateAgentDetailsRequest request) {
        SupportAgentDto updated = agentService.updateAgentDetails(id, request);
        return ResponseEntity.ok(ApiResponse.success("Officer details updated successfully", updated));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('OPERATIONS_SUPERVISOR', 'CUSTOMER_SERVICE_OFFICER')")
    public ResponseEntity<ApiResponse<SupportAgentDto>> updateAgentStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") AgentStatus status) {
        SupportAgentDto updated = agentService.updateAgentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Agent status updated successfully", updated));
    }

    @PatchMapping("/my-status")
    @PreAuthorize("hasRole('CUSTOMER_SERVICE_OFFICER')")
    public ResponseEntity<ApiResponse<SupportAgentDto>> updateMyStatus(
            org.springframework.security.core.Authentication authentication,
            @RequestParam("status") AgentStatus status) {
        SupportAgentDto updated = agentService.updateMyStatus(authentication.getName(), status);
        return ResponseEntity.ok(ApiResponse.success("Officer status updated to " + status, updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> deleteAgent(@PathVariable("id") Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.ok(ApiResponse.success("Agent deleted successfully"));
    }
}
