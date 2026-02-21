package com.example.eldercare.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiningRecordDTO {
    private Long id;
    private Long elderId;
    private String elderName;
    private String mealType;
    private String mealName;
    private String nutritionLevel;
    private String notes;
    private Integer status;
    private LocalDate diningDate;
}
