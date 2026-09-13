package com.customersupport.SupportHUB.knowledgebase;

import com.customersupport.SupportHUB.category.TicketCategory;
import com.customersupport.SupportHUB.category.TicketCategoryRepository;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.common.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    private final KnowledgeBaseArticleRepository kbRepository;
    private final TicketCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public KnowledgeBaseServiceImpl(
            KnowledgeBaseArticleRepository kbRepository,
            TicketCategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.kbRepository = kbRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public KnowledgeBaseArticleDto createArticle(CreateKBRequest request, String createdByEmail) {
        User createdBy = userRepository.findByEmail(createdByEmail).orElse(null);
        TicketCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }

        KnowledgeBaseArticle kb = new KnowledgeBaseArticle(
                request.getTitle(),
                request.getContent(),
                category,
                request.getTags(),
                createdBy
        );
        kb.setPublished(request.isPublished());

        KnowledgeBaseArticle saved = kbRepository.save(kb);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public KnowledgeBaseArticleDto getArticleById(Long id) {
        KnowledgeBaseArticle kb = kbRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge base article not found with id: " + id));
        kb.setViewCount(kb.getViewCount() + 1);
        return mapToDto(kbRepository.save(kb));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeBaseArticleDto> getAllArticles() {
        return kbRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeBaseArticleDto> getPublishedArticles() {
        return kbRepository.findByPublishedTrue().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeBaseArticleDto> getArticlesByCategory(Long categoryId) {
        return kbRepository.findByCategoryIdAndPublishedTrue(categoryId).stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeBaseArticleDto> searchArticles(String query) {
        return kbRepository.searchKnowledgeBase(query).stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional
    public KnowledgeBaseArticleDto updateArticle(Long id, CreateKBRequest request) {
        KnowledgeBaseArticle kb = kbRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge base article not found with id: " + id));

        if (request.getCategoryId() != null) {
            TicketCategory category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            kb.setCategory(category);
        }

        kb.setTitle(request.getTitle());
        kb.setContent(request.getContent());
        kb.setTags(request.getTags());
        kb.setPublished(request.isPublished());

        return mapToDto(kbRepository.save(kb));
    }

    @Override
    @Transactional
    public KnowledgeBaseArticleDto toggleArticlePublished(Long id) {
        KnowledgeBaseArticle kb = kbRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge base article not found with id: " + id));
        kb.setPublished(!kb.isPublished());
        return mapToDto(kbRepository.save(kb));
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        KnowledgeBaseArticle kb = kbRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Knowledge base article not found with id: " + id));
        kbRepository.delete(kb);
    }

    private KnowledgeBaseArticleDto mapToDto(KnowledgeBaseArticle kb) {
        KnowledgeBaseArticleDto dto = new KnowledgeBaseArticleDto();
        dto.setId(kb.getId());
        dto.setTitle(kb.getTitle());
        dto.setContent(kb.getContent());
        if (kb.getCategory() != null) {
            dto.setCategoryId(kb.getCategory().getId());
            dto.setCategoryName(kb.getCategory().getName());
        }
        dto.setPublished(kb.isPublished());
        dto.setTags(kb.getTags());
        dto.setViewCount(kb.getViewCount());
        if (kb.getCreatedBy() != null) {
            dto.setCreatedByName(kb.getCreatedBy().getEmail());
        }
        dto.setCreatedAt(kb.getCreatedAt());
        dto.setUpdatedAt(kb.getUpdatedAt());
        return dto;
    }
}
