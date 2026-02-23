package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.MealPlanDTO;
import com.example.eldercare.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @GetMapping
    public Result<PageResult<MealPlanDTO>> getMealPlans(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String mealType,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<MealPlanDTO> result = mealPlanService.getMealPlans(startDate, endDate, mealType, page, size);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<MealPlanDTO> getMealPlanById(@PathVariable Long id) {
        return Result.success(mealPlanService.getMealPlanById(id));
    }

    @GetMapping("/date/{date}")
    public Result<List<MealPlanDTO>> getMealPlansByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(mealPlanService.getMealPlansByDate(date));
    }

    @GetMapping("/weekly")
    public Result<Map<String, Object>> getWeeklyMealPlan(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return Result.success(mealPlanService.getWeeklyMealPlan(startDate));
    }

    @PostMapping
    public Result<MealPlanDTO> createMealPlan(
            @RequestPart("data") MealPlanDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        return Result.success(mealPlanService.createMealPlan(dto, imageFile));
    }

    @PutMapping("/{id}")
    public Result<MealPlanDTO> updateMealPlan(
            @PathVariable Long id,
            @RequestPart("data") MealPlanDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        return Result.success(mealPlanService.updateMealPlan(id, dto, imageFile));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMealPlan(@PathVariable Long id) {
        mealPlanService.deleteMealPlan(id);
        return Result.success();
    }

    @GetMapping("/{id}/feedbacks")
    public Result<List<com.example.eldercare.dto.MealFeedbackDTO>> getFeedbacksByMealPlan(@PathVariable Long id) {
        return Result.success(mealPlanService.getFeedbacksByMealPlan(id));
    }
}
