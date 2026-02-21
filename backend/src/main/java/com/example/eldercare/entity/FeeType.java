package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fee_type")
public class FeeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fee_type", nullable = false, unique = true, length = 20)
    private String feeType;

    @Column(name = "fee_name", nullable = false, length = 50)
    private String feeName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(length = 10)
    private String unit;

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
}
