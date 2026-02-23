package com.example.eldercare.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MealFeedbackDTO {

    private Long id;

    private Long mealPlanId;

    private String mealName;

    private Long elderId;

    private String elderName;

    private Long userId;

    private String userName;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    private String comment;

    private String feedbackType;

    private String feedbackContent;
}
