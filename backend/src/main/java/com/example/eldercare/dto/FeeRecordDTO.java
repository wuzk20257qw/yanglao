package com.example.eldercare.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FeeRecordDTO {

    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    @NotNull(message = "费用类型不能为空")
    private String feeType;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    private LocalDate feeDate;

    private String period;

    private String notes;

    private Integer status;
}
