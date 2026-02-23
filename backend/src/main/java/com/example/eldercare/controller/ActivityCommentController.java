package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ActivityCommentDTO;
import com.example.eldercare.service.ActivityCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity-comments")
@RequiredArgsConstructor
public class ActivityCommentController {

    private final ActivityCommentService activityCommentService;

    @GetMapping("/post/{postId}")
    public Result<List<ActivityCommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return Result.success(activityCommentService.getCommentsByPostId(postId));
    }

    @GetMapping
    public Result<PageResult<ActivityCommentDTO>> getComments(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<ActivityCommentDTO> result = activityCommentService.getComments(userId, page, size);
        return Result.success(PageResult.of(result));
    }

    @PostMapping
    public Result<ActivityCommentDTO> createComment(
            @RequestBody ActivityCommentDTO dto,
            @RequestParam(required = false) Long elderId,
            HttpServletRequest request) {
        Long currentUserId = 1L;
        Long currentElderId = elderId;
        return Result.success(activityCommentService.createComment(dto, currentUserId, currentElderId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        activityCommentService.deleteComment(id);
        return Result.success();
    }

    @GetMapping("/summary")
    public Result<Map<String, Object>> getCommentSummary(@RequestParam Long elderId) {
        return Result.success(activityCommentService.getCommentSummary(elderId));
    }
}
