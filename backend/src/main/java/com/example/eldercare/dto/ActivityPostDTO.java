package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityPostDTO {

    private Long id;

    private Long elderId;

    private String elderName;

    @NotBlank(message = "活动类型不能为空")
    private String activityType;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String content;

    private List<String> images;

    private List<String> videos;

    private String location;

    private Integer participantCount;

    private LocalDateTime activityDate;

    private List<String> tags;

    private Integer viewCount;

    private Integer likeCount;

    private Integer commentCount;

    private Boolean isPinned;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isLiked;

    private String elderAvatar;
}
