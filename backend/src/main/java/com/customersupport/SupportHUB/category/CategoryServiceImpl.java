package com.customersupport.SupportHUB.category;

import com.customersupport.SupportHUB.agent.SupportAgent;
import com.customersupport.SupportHUB.agent.SupportAgentRepository;
import com.customersupport.SupportHUB.common.BadRequestException;
import com.customersupport.SupportHUB.common.DuplicateResourceException;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.ticket.TicketRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final TicketCategoryRepository categoryRepository;
    private final TicketRepository ticketRepository;
    private final SupportAgentRepository agentRepository;

    public CategoryServiceImpl(TicketCategoryRepository categoryRepository,
                               TicketRepository ticketRepository,
                               SupportAgentRepository agentRepository) {
        this.categoryRepository = categoryRepository;
        this.ticketRepository = ticketRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    @Transactional
    public TicketCategoryDto createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        TicketCategory category = new TicketCategory(request.getName(), request.getDescription());
        TicketCategory saved = categoryRepository.save(category);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketCategoryDto getCategoryById(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketCategoryDto> getActiveCategories() {
        return categoryRepository.findByActiveTrue().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional
    public TicketCategoryDto updateCategory(Long id, CreateCategoryRequest request) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equalsIgnoreCase(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category with name '" + request.getName() + "' already exists");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        TicketCategory updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public TicketCategoryDto toggleCategoryActive(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setActive(!category.isActive());
        return mapToDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        long ticketCount = ticketRepository.countByCategoryId(id);
        if (ticketCount > 0) {
            throw new BadRequestException("Cannot delete category with " + ticketCount + " associated tickets. Deactivate the category instead.");
        }

        // Unassign category from mapped agents to prevent foreign key constraint violations
        List<SupportAgent> agents = agentRepository.findByCategoryId(id);
        for (SupportAgent agent : agents) {
            agent.getAssignedCategories().remove(category);
            agentRepository.save(agent);
        }

        categoryRepository.delete(category);
    }

    private TicketCategoryDto mapToDto(TicketCategory c) {
        TicketCategoryDto dto = new TicketCategoryDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setActive(c.isActive());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        return dto;
    }
}
