package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "nursing_record")
public class NursingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "nursing_type", nullable = false, length = 20)
    private String nursingType;

    @Column(name = "nursing_level", length = 20)
    private String nursingLevel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String notes;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column(name = "record_time")
    private LocalTime recordTime;

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
