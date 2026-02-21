package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fee_record")
public class FeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "fee_type", length = 20)
    private String feeType;

    @Column(name = "fee_name", length = 50)
    private String feeName;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "fee_date", nullable = false)
    private LocalDate feeDate;

    @Column(length = 20)
    private String period;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(name = "pay_time")
    private LocalDateTime payTime;

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
