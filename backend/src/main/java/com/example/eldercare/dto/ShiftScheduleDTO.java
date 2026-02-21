package com.example.eldercare.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShiftScheduleDTO {
    private Long id;
    private Long nurseId;
    private String nurseName;
    private String shiftType;
    private LocalDate shiftDate;
    private String startTime;
    private String endTime;
    private String notes;
    private Integer status;
}
