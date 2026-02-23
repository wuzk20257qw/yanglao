package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MealPlanDTO {

    private Long id;

    @NotBlank(message = "餐品名称不能为空")
    private String mealName;

    @NotBlank(message = "餐品类型不能为空")
    private String mealType;

    @NotNull(message = "用餐日期不能为空")
    private LocalDate mealDate;

    private String imageUrl;

    private String description;

    private String ingredients;

    private Integer calories;

    private Integer protein;

    private Integer carbs;

    private Integer fat;

    private Integer fiber;

    private String tags;

    private Integer status;

    private Double averageRating;

    private Long feedbackCount;
}
