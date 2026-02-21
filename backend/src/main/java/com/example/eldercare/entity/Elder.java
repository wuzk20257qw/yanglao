package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "elder_info")
public class Elder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(unique = true, length = 18)
    private String idCard;

    @Column(length = 20)
    private String phone;

    @Column(length = 200)
    private String address;

    @Column(name = "nursing_level", length = 20)
    private String nursingLevel;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "bed_id")
    private Long bedId;

    @Column(name = "emergency_contact", length = 50)
    private String emergencyContact;

    @Column(name = "emergency_phone", length = 20)
    private String emergencyPhone;

    @Column(name = "health_status", columnDefinition = "TEXT")
    private String healthStatus;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Integer getAge() {
        if (birthDate == null) {
            return null;
        }
        LocalDate now = LocalDate.now();
        return now.getYear() - birthDate.getYear();
    }

    public Long getAdmissionDays() {
        if (admissionDate == null) {
            return null;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(admissionDate, LocalDate.now());
    }
}
