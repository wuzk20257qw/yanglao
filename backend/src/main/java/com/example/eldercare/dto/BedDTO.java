package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BedDTO {

    @NotBlank(message = "床位号不能为空")
    private String bedNo;

    @NotBlank(message = "房间号不能为空")
    private String roomNo;

    private String building;

    private Integer floor;

    private String type;

    private BigDecimal price;
}
