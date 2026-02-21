package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NursingRecordDTO {

    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    @NotNull(message = "护理类型不能为空")
    private String nursingType;

    private String nursingLevel;

    private String content;

    private String notes;

    private LocalDate recordDate;

    private LocalTime recordTime;
}
