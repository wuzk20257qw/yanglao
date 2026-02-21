package com.example.eldercare.dto;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String content;
    private Boolean isRead;
    private String relatedType;
    private Long relatedId;
    private LocalDateTime createTime;
    private LocalDateTime readTime;
}
