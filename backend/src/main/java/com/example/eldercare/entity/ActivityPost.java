package com.example.eldercare.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_post")
public class ActivityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "elder_id", nullable = false)
    private Long elderId;

    @Column(name = "elder_name", length = 50)
    private String elderName;

    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "images", length = 1000)
    private String images;

    @Column(name = "videos", length = 1000)
    private String videos;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "activity_date")
    private LocalDateTime activityDate;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @Column(name = "is_pinned")
    private Boolean isPinned = false;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
