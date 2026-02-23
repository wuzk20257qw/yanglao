package com.example.eldercare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityCommentDTO {

    private Long id;

    private Long postId;

    private Long parentId;

    private Long userId;

    private String userName;

    private String userAvatar;

    private Long elderId;

    private String elderName;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    private Long replyToUserId;

    private String replyToUserName;

    private Integer likeCount;

    private Integer status;

    private LocalDateTime createTime;

    private List<ActivityCommentDTO> replies;
}
