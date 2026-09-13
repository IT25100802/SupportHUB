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
public class FaqServiceImpl implements FaqService {

    private final FaqArticleRepository faqRepository;
    private final TicketCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public FaqServiceImpl(
            FaqArticleRepository faqRepository,
            TicketCategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.faqRepository = faqRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FaqArticleDto createFaq(CreateFaqRequest request, String createdByEmail) {
        User createdBy = userRepository.findByEmail(createdByEmail).orElse(null);
        TicketCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }

        FaqArticle faq = new FaqArticle(
                request.getQuestion(),
                request.getAnswer(),
                category,
                request.getKeywords(),
                createdBy
        );
        faq.setPublished(request.isPublished());

        FaqArticle saved = faqRepository.save(faq);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public FaqArticleDto getFaqById(Long id) {
        FaqArticle faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ article not found with id: " + id));
        faq.setViewCount(faq.getViewCount() + 1);
        return mapToDto(faqRepository.save(faq));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqArticleDto> getAllFaqs() {
        return faqRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqArticleDto> getPublishedFaqs() {
        return faqRepository.findByPublishedTrue().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqArticleDto> getFaqsByCategory(Long categoryId) {
        return faqRepository.findByCategoryIdAndPublishedTrue(categoryId).stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqArticleDto> searchFaqs(String query) {
        return faqRepository.searchFaqs(query).stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqArticleDto> getPopularFaqs() {
        return faqRepository.findTop5ByPublishedTrueOrderByViewCountDesc().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional
    public FaqArticleDto updateFaq(Long id, CreateFaqRequest request) {
        FaqArticle faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ article not found with id: " + id));

        if (request.getCategoryId() != null) {
            TicketCategory category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            faq.setCategory(category);
        }

        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setKeywords(request.getKeywords());
        faq.setPublished(request.isPublished());

        return mapToDto(faqRepository.save(faq));
    }

    @Override
    @Transactional
    public FaqArticleDto toggleFaqPublished(Long id) {
        FaqArticle faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ article not found with id: " + id));
        faq.setPublished(!faq.isPublished());
        return mapToDto(faqRepository.save(faq));
    }

    @Override
    @Transactional
    public void deleteFaq(Long id) {
        FaqArticle faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ article not found with id: " + id));
        faqRepository.delete(faq);
    }

    private FaqArticleDto mapToDto(FaqArticle f) {
        FaqArticleDto dto = new FaqArticleDto();
        dto.setId(f.getId());
        dto.setQuestion(f.getQuestion());
        dto.setAnswer(f.getAnswer());
        if (f.getCategory() != null) {
            dto.setCategoryId(f.getCategory().getId());
            dto.setCategoryName(f.getCategory().getName());
        }
        dto.setPublished(f.isPublished());
        dto.setKeywords(f.getKeywords());
        dto.setViewCount(f.getViewCount());
        if (f.getCreatedBy() != null) {
            dto.setCreatedByName(f.getCreatedBy().getEmail());
        }
        dto.setCreatedAt(f.getCreatedAt());
        dto.setUpdatedAt(f.getUpdatedAt());
        return dto;
    }
}
