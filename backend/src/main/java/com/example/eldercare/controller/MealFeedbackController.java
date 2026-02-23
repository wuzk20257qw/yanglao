package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.MealFeedbackDTO;
import com.example.eldercare.service.MealFeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meal-feedbacks")
@RequiredArgsConstructor
public class MealFeedbackController {

    private final MealFeedbackService mealFeedbackService;

    @GetMapping
    public Result<PageResult<MealFeedbackDTO>> getFeedbacks(
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<MealFeedbackDTO> result = mealFeedbackService.getFeedbacks(elderId, userId, page, size);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/recent")
    public Result<List<MealFeedbackDTO>> getRecentFeedbacks(@RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(mealFeedbackService.getRecentFeedbacks(limit));
    }

    @PostMapping
    public Result<MealFeedbackDTO> submitFeedback(
            @RequestBody MealFeedbackDTO dto,
            HttpServletRequest request) {
        Long currentUserId = 1L;
        return Result.success(mealFeedbackService.submitFeedback(dto, currentUserId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteFeedback(@PathVariable Long id) {
        mealFeedbackService.deleteFeedback(id);
        return Result.success();
    }

    @GetMapping("/summary")
    public Result<Map<String, Object>> getFeedbackSummary(@RequestParam Long elderId) {
        return Result.success(mealFeedbackService.getFeedbackSummary(elderId));
    }
}
