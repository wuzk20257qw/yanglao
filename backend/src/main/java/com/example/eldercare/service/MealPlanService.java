package com.example.eldercare.service;

import com.example.eldercare.dto.MealPlanDTO;
import com.example.eldercare.entity.MealPlan;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final MealFeedbackRepository mealFeedbackRepository;

    private static final String UPLOAD_DIR = "uploads/meals/";

    public Page<MealPlanDTO> getMealPlans(
            LocalDate startDate,
            LocalDate endDate,
            String mealType,
            Integer page,
            Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "mealDate", "mealType"));

        Page<MealPlan> mealPlanPage;

        if (startDate != null && endDate != null) {
            mealPlanPage = mealPlanRepository.findByMealDateBetweenOrderByMealDateDescMealTypeDesc(startDate, endDate, pageable);
        } else if (startDate != null) {
            mealPlanPage = mealPlanRepository.findFromToday(startDate, pageable);
        } else {
            mealPlanPage = mealPlanRepository.findAll(pageable);
        }

        return mealPlanPage.map(this::toDTOWithRating);
    }

    public MealPlanDTO getMealPlanById(Long id) {
        MealPlan mealPlan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐品计划不存在"));
        return toDTOWithRating(mealPlan);
    }

    public List<MealPlanDTO> getMealPlansByDate(LocalDate mealDate) {
        List<MealPlan> mealPlans = mealPlanRepository.findByMealDateOrderByMealType(mealDate);
        return mealPlans.stream().map(this::toDTOWithRating).collect(Collectors.toList());
    }

    public Map<String, Object> getWeeklyMealPlan(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        List<MealPlan> mealPlans = mealPlanRepository.findBetweenDates(startDate, endDate);

        Map<String, List<MealPlanDTO>> weeklyPlan = mealPlans.stream()
                .collect(Collectors.groupingBy(
                        mealPlan -> mealPlan.getMealDate().toString(),
                        Collectors.mapping(this::toDTOWithRating, Collectors.toList())
                ));

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("meals", weeklyPlan);
        return result;
    }

    @Transactional
    public MealPlanDTO createMealPlan(MealPlanDTO dto, MultipartFile imageFile) throws IOException {
        MealPlan mealPlan = new MealPlan();

        mealPlan.setMealName(dto.getMealName());
        mealPlan.setMealType(dto.getMealType());
        mealPlan.setMealDate(dto.getMealDate());
        mealPlan.setDescription(dto.getDescription());
        mealPlan.setIngredients(dto.getIngredients());
        mealPlan.setCalories(dto.getCalories());
        mealPlan.setProtein(dto.getProtein());
        mealPlan.setCarbs(dto.getCarbs());
        mealPlan.setFat(dto.getFat());
        mealPlan.setFiber(dto.getFiber());
        mealPlan.setTags(dto.getTags());
        mealPlan.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            mealPlan.setImageUrl(imageUrl);
        }

        MealPlan saved = mealPlanRepository.save(mealPlan);
        return toDTOWithRating(saved);
    }

    @Transactional
    public MealPlanDTO updateMealPlan(Long id, MealPlanDTO dto, MultipartFile imageFile) throws IOException {
        MealPlan mealPlan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐品计划不存在"));

        if (dto.getMealName() != null) {
            mealPlan.setMealName(dto.getMealName());
        }
        if (dto.getMealType() != null) {
            mealPlan.setMealType(dto.getMealType());
        }
        if (dto.getMealDate() != null) {
            mealPlan.setMealDate(dto.getMealDate());
        }
        if (dto.getDescription() != null) {
            mealPlan.setDescription(dto.getDescription());
        }
        if (dto.getIngredients() != null) {
            mealPlan.setIngredients(dto.getIngredients());
        }
        if (dto.getCalories() != null) {
            mealPlan.setCalories(dto.getCalories());
        }
        if (dto.getProtein() != null) {
            mealPlan.setProtein(dto.getProtein());
        }
        if (dto.getCarbs() != null) {
            mealPlan.setCarbs(dto.getCarbs());
        }
        if (dto.getFat() != null) {
            mealPlan.setFat(dto.getFat());
        }
        if (dto.getFiber() != null) {
            mealPlan.setFiber(dto.getFiber());
        }
        if (dto.getTags() != null) {
            mealPlan.setTags(dto.getTags());
        }
        if (dto.getStatus() != null) {
            mealPlan.setStatus(dto.getStatus());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            mealPlan.setImageUrl(imageUrl);
        }

        MealPlan saved = mealPlanRepository.save(mealPlan);
        return toDTOWithRating(saved);
    }

    @Transactional
    public void deleteMealPlan(Long id) {
        MealPlan mealPlan = mealPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐品计划不存在"));

        if (mealPlan.getImageUrl() != null) {
            deleteImage(mealPlan.getImageUrl());
        }

        mealPlanRepository.deleteById(id);
    }

    public List<MealFeedbackDTO> getFeedbacksByMealPlan(Long mealPlanId) {
        return mealFeedbackRepository.findByMealPlanId(mealPlanId).stream()
                .map(this::feedbackToDTO)
                .collect(Collectors.toList());
    }

    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + File.separator + UPLOAD_DIR;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        return "/api/files/meals/" + newFilename;
    }

    private void deleteImage(String imageUrl) {
        try {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR, filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("删除图片失败: {}", imageUrl, e);
        }
    }

    private MealPlanDTO toDTOWithRating(MealPlan mealPlan) {
        MealPlanDTO dto = new MealPlanDTO();
        dto.setId(mealPlan.getId());
        dto.setMealName(mealPlan.getMealName());
        dto.setMealType(mealPlan.getMealType());
        dto.setMealDate(mealPlan.getMealDate());
        dto.setImageUrl(mealPlan.getImageUrl());
        dto.setDescription(mealPlan.getDescription());
        dto.setIngredients(mealPlan.getIngredients());
        dto.setCalories(mealPlan.getCalories());
        dto.setProtein(mealPlan.getProtein());
        dto.setCarbs(mealPlan.getCarbs());
        dto.setFat(mealPlan.getFat());
        dto.setFiber(mealPlan.getFiber());
        dto.setTags(mealPlan.getTags());
        dto.setStatus(mealPlan.getStatus());

        Double avgRating = mealFeedbackRepository.getAverageRating(mealPlan.getId());
        Long feedbackCount = mealFeedbackRepository.getFeedbackCount(mealPlan.getId());

        dto.setAverageRating(avgRating);
        dto.setFeedbackCount(feedbackCount);

        return dto;
    }

    private MealFeedbackDTO feedbackToDTO(com.example.eldercare.entity.MealFeedback feedback) {
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
