package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alarm_record")
public class AlarmRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elder_id")
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "elder_phone", length = 20)
    private String elderPhone;

    @Column(name = "alarm_type", nullable = false, length = 20)
    private String alarmType;

    @Column(name = "alarm_level", length = 20)
    private String alarmLevel;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Integer status = 0;

    @Column(name = "handle_by", length = 50)
    private String handleBy;

    @Column(name = "handle_notes", length = 500)
    private String handleNotes;

    @Column(name = "handle_time")
    private LocalDateTime handleTime;

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
