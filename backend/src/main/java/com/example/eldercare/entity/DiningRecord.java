package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dining_record")
public class DiningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "meal_type", nullable = false, length = 20)
    private String mealType;

    @Column(name = "meal_name", nullable = false, length = 100)
    private String mealName;

    @Column(name = "nutrition_level", length = 20)
    private String nutritionLevel;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(name = "dining_date", nullable = false)
    private LocalDate diningDate;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
