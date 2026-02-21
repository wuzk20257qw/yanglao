package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ElderCreateDTO {
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    private Integer gender;

    private LocalDate birthDate;

    @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式不正确")
    private String idCard;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String address;

    private String nursingLevel;

    private LocalDate admissionDate;

    private String emergencyContact;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "紧急联系电话格式不正确")
    private String emergencyPhone;

    private String healthStatus;

    private String allergies;
}
