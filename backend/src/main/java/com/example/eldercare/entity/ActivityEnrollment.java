package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_enrollment")
public class ActivityEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "activity_name", length = 100)
    private String activityName;

    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "enrollment_time", nullable = false)
    private LocalDateTime enrollmentTime;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (enrollmentTime == null) {
            enrollmentTime = LocalDateTime.now();
        }
    }
}
