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

        TicketCategory category = new TicketCategory(request.getName(), request.getDescription(), request.getIcon());
        if (request.getParentId() != null) {
            TicketCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }

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
        // Return ONLY main/root categories (where parent is null)
        List<TicketCategory> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketCategoryDto> getActiveCategories() {
        // Return ONLY active main/root categories (where parent is null and active is true)
        List<TicketCategory> rootActive = categoryRepository.findByParentIsNullAndActiveTrue();
        return rootActive.stream().map(this::mapToDto).toList();
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
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        TicketCategory updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public TicketCategoryDto toggleCategoryActive(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        boolean newStatus = !category.isActive();

        // If trying to activate a subcategory whose parent is inactive, prevent it
        if (newStatus && category.getParent() != null && !category.getParent().isActive()) {
            throw new BadRequestException("Cannot activate subcategory while its parent category '" + category.getParent().getName() + "' is inactive. Please activate the parent category first.");
        }

        category.setActive(newStatus);
        TicketCategory saved = categoryRepository.save(category);

        // Cascade active status to all subcategories when toggling a parent category
        List<TicketCategory> subcategories = categoryRepository.findByParentId(id);
        if (!subcategories.isEmpty()) {
            for (TicketCategory sub : subcategories) {
                sub.setActive(newStatus);
                categoryRepository.save(sub);
            }
        }

        return mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        TicketCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Safety check 1: Cannot delete parent category if it has subcategories
        long subcatCount = categoryRepository.countByParentId(id);
        if (subcatCount > 0) {
            throw new BadRequestException("Cannot delete category with " + subcatCount + " associated subcategories. Please delete or reassign subcategories first, or deactivate the category.");
        }

        // Safety check 2: Cannot delete category if it has active or historical tickets
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

    @Override
    @Transactional(readOnly = true)
    public List<TicketCategoryDto> getSubcategoriesByCategoryId(Long parentId) {
        if (!categoryRepository.existsById(parentId)) {
            throw new ResourceNotFoundException("Category not found with id: " + parentId);
        }
        return categoryRepository.findByParentId(parentId).stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional
    public TicketCategoryDto createSubcategory(Long parentId, CreateCategoryRequest request) {
        TicketCategory parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + parentId));

        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Subcategory with name '" + request.getName() + "' already exists");
        }

        TicketCategory subcategory = new TicketCategory(
                request.getName(),
                request.getDescription(),
                request.getIcon() != null ? request.getIcon() : parent.getIcon(),
                parent
        );
        subcategory.setActive(parent.isActive());

        TicketCategory saved = categoryRepository.save(subcategory);
        return mapToDto(saved);
    }

    private TicketCategoryDto mapToDto(TicketCategory c) {
        TicketCategoryDto dto = new TicketCategoryDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setIcon(c.getIcon());
        dto.setActive(c.isActive());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());

        if (c.getParent() != null) {
            dto.setParentId(c.getParent().getId());
            dto.setParentName(c.getParent().getName());
        }

        int subCount = (int) categoryRepository.countByParentId(c.getId());
        dto.setSubcategoryCount(subCount);

        long activeTickets = ticketRepository.countActiveTicketsByCategoryId(c.getId());
        dto.setActiveTicketCount(activeTickets);

        int officerCount = agentRepository.findByCategoryId(c.getId()).size();
        dto.setOfficerCount(officerCount);

        return dto;
    }
}
