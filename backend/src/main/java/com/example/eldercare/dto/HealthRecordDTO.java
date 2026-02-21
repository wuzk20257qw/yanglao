package com.example.eldercare.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HealthRecordDTO {
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    @NotNull(message = "收缩压不能为空")
    @DecimalMin(value = "60", message = "收缩压范围60-200")
    @DecimalMax(value = "200", message = "收缩压范围60-200")
    private Integer systolicBP;

    @NotNull(message = "舒张压不能为空")
    @DecimalMin(value = "40", message = "舒张压范围40-130")
    @DecimalMax(value = "130", message = "舒张压范围40-130")
    private Integer diastolicBP;

    @NotNull(message = "心率不能为空")
    @DecimalMin(value = "40", message = "心率范围40-180")
    @DecimalMax(value = "180", message = "心率范围40-180")
    private Integer heartRate;

    @NotNull(message = "血氧不能为空")
    @DecimalMin(value = "70", message = "血氧范围70-100")
    @DecimalMax(value = "100", message = "血氧范围70-100")
    private Integer bloodOxygen;

    @NotNull(message = "血糖不能为空")
    @DecimalMin(value = "2.0", message = "血糖范围2.0-20.0")
    @DecimalMax(value = "20.0", message = "血糖范围2.0-20.0")
    private BigDecimal bloodSugar;

    @NotNull(message = "体温不能为空")
    @DecimalMin(value = "35.0", message = "体温范围35.0-42.0")
    @DecimalMax(value = "42.0", message = "体温范围35.0-42.0")
    private BigDecimal temperature;

    private LocalDateTime recordTime;

    private String remark;
}
