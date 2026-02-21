package com.example.eldercare.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityEnrollmentDTO {
    private Long id;
    private Long activityId;
    private String activityName;
    private Long elderId;
    private String elderName;
    private LocalDateTime enrollmentTime;
    private String notes;
    private Integer status;
}
