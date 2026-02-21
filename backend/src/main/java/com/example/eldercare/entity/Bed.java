package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bed_info")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20, name = "bed_no")
    private String bedNo;

    @Column(nullable = false, length = 20, name = "room_no")
    private String roomNo;

    @Column(length = 20)
    private String building;

    private Integer floor;

    @Column(nullable = false, length = 20)
    private String type;

    private BigDecimal price;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(name = "elder_id")
    private Long elderId;

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
