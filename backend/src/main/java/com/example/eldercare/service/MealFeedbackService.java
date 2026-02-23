package com.example.eldercare.service;

import com.example.eldercare.dto.MealFeedbackDTO;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.entity.MealFeedback;
import com.example.eldercare.entity.MealPlan;
import com.example.eldercare.entity.User;
import com.example.eldercare.repository.ElderRepository;
import com.example.eldercare.repository.MealFeedbackRepository;
import com.example.eldercare.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealFeedbackService {

    private final MealFeedbackRepository mealFeedbackRepository;
    private final MealPlanRepository mealPlanRepository;
    private final ElderRepository elderRepository;

    public Page<MealFeedbackDTO> getFeedbacks(
            Long elderId,
            Long userId,
            Integer page,
            Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<MealFeedback> feedbackPage;

        if (elderId != null) {
            feedbackPage = mealFeedbackRepository.findByElderId(elderId, pageable);
        } else if (userId != null) {
            feedbackPage = mealFeedbackRepository.findByUserId(userId, pageable);
        } else {
            feedbackPage = mealFeedbackRepository.findAll(pageable);
        }

        return feedbackPage.map(this::toDTO);
    }

    public List<MealFeedbackDTO> getRecentFeedbacks(int limit) {
        return mealFeedbackRepository.findRecentFeedback(
                        java.time.LocalDateTime.now().minusDays(7)
                ).stream()
                .limit(limit)
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public MealFeedbackDTO submitFeedback(MealFeedbackDTO dto, Long currentUserId) {
        MealPlan mealPlan = mealPlanRepository.findById(dto.getMealPlanId())
                .orElseThrow(() -> new RuntimeException("餐品计划不存在"));

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new RuntimeException("老人档案不存在"));

        MealFeedback feedback = new MealFeedback();

        feedback.setMealPlanId(dto.getMealPlanId());
        feedback.setMealName(mealPlan.getMealName());
        feedback.setElderId(dto.getElderId());
        feedback.setElderName(elder.getName());
        feedback.setUserId(currentUserId);
        feedback.setUserName("用户" + currentUserId);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setFeedbackType(dto.getFeedbackType());
        feedback.setFeedbackContent(dto.getFeedbackContent());

        MealFeedback saved = mealFeedbackRepository.save(feedback);
        return toDTO(saved);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        mealFeedbackRepository.deleteById(id);
    }

    public Map<String, Object> getFeedbackSummary(Long elderId) {
        List<MealFeedback> feedbacks = mealFeedbackRepository.findByElderIdOrderByCreateTimeDesc(elderId);

        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalFeedbacks", feedbacks.size());

        if (!feedbacks.isEmpty()) {
            double avgRating = feedbacks.stream()
                    .mapToInt(MealFeedback::getRating)
                    .average()
                    .orElse(0);
            summary.put("averageRating", avgRating);

            long positiveCount = feedbacks.stream().filter(f -> f.getRating() >= 4).count();
            long neutralCount = feedbacks.stream().filter(f -> f.getRating() == 3).count();
            long negativeCount = feedbacks.stream().filter(f -> f.getRating() <= 2).count();

            summary.put("positiveCount", positiveCount);
            summary.put("neutralCount", neutralCount);
            summary.put("negativeCount", negativeCount);
        }

        return summary;
    }

    private MealFeedbackDTO toDTO(MealFeedback feedback) {
        MealFeedbackDTO dto = new MealFeedbackDTO();
        dto.setId(feedback.getId());
        dto.setMealPlanId(feedback.getMealPlanId());
        dto.setMealName(feedback.getMealName());
        dto.setElderId(feedback.getElderId());
        dto.setElderName(feedback.getElderName());
        dto.setUserId(feedback.getUserId());
        dto.setUserName(feedback.getUserName());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setFeedbackType(feedback.getFeedbackType());
        dto.setFeedbackContent(feedback.getFeedbackContent());
        return dto;
    }
}
