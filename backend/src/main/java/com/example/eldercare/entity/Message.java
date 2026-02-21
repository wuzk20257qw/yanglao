package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "related_type", length = 50)
    private String relatedType;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "read_time")
    private LocalDateTime readTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (isRead == null) {
            isRead = false;
        }
    }
}
