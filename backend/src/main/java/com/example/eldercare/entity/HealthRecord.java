package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "health_record")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "record_type", nullable = false, length = 20)
    private String recordType;

    @Column(name = "blood_pressure", length = 50)
    private String bloodPressure;

    private Integer heartRate;

    private BigDecimal bloodSugar;

    private BigDecimal bodyTemperature;

    private BigDecimal weight;

    @Column(length = 500)
    private String medication;

    @Column(length = 500)
    private String notes;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column(name = "record_time")
    private LocalDateTime recordTime;

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
}
