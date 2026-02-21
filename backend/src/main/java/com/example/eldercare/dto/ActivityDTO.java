package com.example.eldercare.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityDTO {
    private Long id;
    private String name;
    private String description;
    private String activityType;
    private LocalDate activityDate;
    private String startTime;
    private String endTime;
    private String location;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Double feeAmount;
    private Integer status;
}
