package com.customersupport.SupportHUB.agent;

import com.customersupport.SupportHUB.category.TicketCategory;
import com.customersupport.SupportHUB.category.TicketCategoryDto;
import com.customersupport.SupportHUB.category.TicketCategoryRepository;
import com.customersupport.SupportHUB.common.BadRequestException;
import com.customersupport.SupportHUB.common.DuplicateResourceException;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.common.Role;
import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.common.UserRepository;
import com.customersupport.SupportHUB.ticket.TicketRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private final SupportAgentRepository agentRepository;
    private final UserRepository userRepository;
    private final TicketCategoryRepository categoryRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentServiceImpl(
            SupportAgentRepository agentRepository,
            UserRepository userRepository,
            TicketCategoryRepository categoryRepository,
            TicketRepository ticketRepository,
            PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public SupportAgentDto createAgent(CreateAgentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already in use: " + request.getEmail());
        }

        Role role = Role.CUSTOMER_SERVICE_OFFICER;
        if (request.getRole() != null && !request.getRole().trim().isEmpty()) {
            try {
                role = Role.valueOf(request.getRole().trim().toUpperCase());
            } catch (Exception ignored) {}
        }

        String empCode = request.getEmployeeCode();
        if (empCode == null || empCode.trim().isEmpty()) {
            empCode = (role == Role.CUSTOMER_SERVICE_OFFICER ? "AGT-" : "EMP-") + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }

        if (agentRepository.existsByEmployeeCode(empCode)) {
            empCode = (role == Role.CUSTOMER_SERVICE_OFFICER ? "AGT-" : "EMP-") + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), role);
        user = userRepository.save(user);

        SupportAgent agent = new SupportAgent(user, request.getFullName(), request.getPhone(), empCode);

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<TicketCategory> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
            agent.setAssignedCategories(categories);
        }

        SupportAgent saved = agentRepository.save(agent);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportAgentDto getAgentById(Long id) {
        SupportAgent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + id));
        return mapToDto(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportAgentDto getAgentByUserId(Long userId) {
        SupportAgent agent = agentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found for user id: " + userId));
        return mapToDto(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportAgentDto> getAllAgents() {
        return agentRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportAgentDto> getAgentsByCategory(Long categoryId) {
        return agentRepository.findByCategoryId(categoryId).stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional
    public SupportAgentDto updateAgentCategories(Long agentId, UpdateAgentCategoriesRequest request) {
        SupportAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

        if (request.getCategoryIds() != null) {
            Set<TicketCategory> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
            agent.setAssignedCategories(categories);
        } else {
            agent.getAssignedCategories().clear();
        }

        return mapToDto(agentRepository.save(agent));
    }

    @Override
    @Transactional
    public SupportAgentDto updateAgentStatus(Long agentId, AgentStatus status) {
        SupportAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));
        agent.setStatus(status);
        return mapToDto(agentRepository.save(agent));
    }

    @Override
    @Transactional
    public SupportAgentDto updateAgentDetails(Long agentId, UpdateAgentDetailsRequest request) {
        SupportAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            agent.setFullName(request.getFullName().trim());
        }

        if (request.getPhone() != null) {
            agent.setPhone(request.getPhone().trim());
        }

        if (request.getStatus() != null) {
            agent.setStatus(request.getStatus());
        }

        if (request.getRole() != null && !request.getRole().trim().isEmpty() && agent.getUser() != null) {
            try {
                Role newRole = com.customersupport.SupportHUB.common.Role.valueOf(request.getRole().trim().toUpperCase());
                agent.getUser().setRole(newRole);
                userRepository.save(agent.getUser());
            } catch (Exception e) {
                // Log or ignore invalid role
            }
        }

        if (request.getActive() != null && agent.getUser() != null) {
            agent.getUser().setActive(request.getActive());
            if (!request.getActive()) {
                agent.setStatus(AgentStatus.OFFLINE);
            }
            userRepository.save(agent.getUser());
        }

        if (request.getCategoryIds() != null) {
            Set<TicketCategory> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
            agent.setAssignedCategories(categories);
        }

        userRepository.save(agent.getUser());
        SupportAgent saved = agentRepository.save(agent);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public SupportAgentDto updateMyStatus(String email, AgentStatus status) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        SupportAgent agent = agentRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Agent profile not found for user"));
        agent.setStatus(status);
        SupportAgent saved = agentRepository.save(agent);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteAgent(Long agentId) {
        SupportAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found with id: " + agentId));
        
        long activeTickets = ticketRepository.countActiveTicketsForAgent(agentId);
        if (activeTickets > 0) {
            throw new BadRequestException("Cannot delete staff member with " + activeTickets + " active assigned tickets. Reassign tickets or deactivate account instead.");
        }

        User user = agent.getUser();
        agent.getAssignedCategories().clear();
        agentRepository.save(agent);
        agentRepository.delete(agent);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    private SupportAgentDto mapToDto(SupportAgent agent) {
        SupportAgentDto dto = new SupportAgentDto();
        dto.setId(agent.getId());
        dto.setUserId(agent.getUser().getId());
        dto.setEmail(agent.getUser().getEmail());
        dto.setFullName(agent.getFullName());
        dto.setPhone(agent.getPhone());
        dto.setEmployeeCode(agent.getEmployeeCode());
        if (agent.getUser() != null) {
            if (agent.getUser().getRole() != null) {
                dto.setRole(agent.getUser().getRole().name());
            }
            dto.setActive(agent.getUser().isActive());
        }
        dto.setStatus(agent.getStatus());
        dto.setCreatedAt(agent.getCreatedAt());

        Set<TicketCategoryDto> categories = agent.getAssignedCategories().stream().map(c -> {
            TicketCategoryDto catDto = new TicketCategoryDto();
            catDto.setId(c.getId());
            catDto.setName(c.getName());
            catDto.setDescription(c.getDescription());
            catDto.setActive(c.isActive());
            return catDto;
        }).collect(Collectors.toSet());
        dto.setAssignedCategories(categories);

        long activeCount = ticketRepository.countActiveTicketsForAgent(agent.getId());
        dto.setActiveTicketCount(activeCount);

        return dto;
    }
}
