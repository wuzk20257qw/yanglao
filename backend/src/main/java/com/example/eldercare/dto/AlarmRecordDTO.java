package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmRecordDTO {

    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    @NotNull(message = "告警类型不能为空")
    private String alarmType;

    private String alarmLevel;

    private String content;

    private Integer status;

    private String handleBy;

    private String handleNotes;

    private LocalDateTime handleTime;
}
