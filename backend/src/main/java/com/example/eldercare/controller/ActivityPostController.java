package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ActivityPostDTO;
import com.example.eldercare.service.ActivityPostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/activity-posts")
@RequiredArgsConstructor
public class ActivityPostController {

    private final ActivityPostService activityPostService;

    @GetMapping
    public Result<PageResult<ActivityPostDTO>> getPosts(
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Long currentUserId = 1L;
        Page<ActivityPostDTO> result = activityPostService.getPosts(elderId, activityType, keyword, page, size, currentUserId);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/pinned")
    public Result<List<ActivityPostDTO>> getPinnedPosts(HttpServletRequest request) {
        Long currentUserId = 1L;
        List<ActivityPostDTO> result = activityPostService.getPinnedPosts(currentUserId);
        return Result.success(result);
    }

    @GetMapping("/hot")
    public Result<List<ActivityPostDTO>> getHotPosts(HttpServletRequest request) {
        Long currentUserId = 1L;
        List<ActivityPostDTO> result = activityPostService.getHotPosts(currentUserId);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ActivityPostDTO> getPostById(@PathVariable Long id, HttpServletRequest request) {
        Long currentUserId = 1L;
        return Result.success(activityPostService.getPostById(id, currentUserId));
    }

    @PostMapping
    public Result<ActivityPostDTO> createPost(
            @RequestPart("data") ActivityPostDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videoFiles,
            HttpServletRequest request) throws Exception {
        Long currentUserId = 1L;
        Long currentElderId = dto.getElderId();
        return Result.success(activityPostService.createPost(dto, imageFiles, videoFiles, currentUserId));
    }

    @PutMapping("/{id}")
    public Result<ActivityPostDTO> updatePost(
            @PathVariable Long id,
            @RequestPart("data") ActivityPostDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videoFiles,
            HttpServletRequest request) throws Exception {
        Long currentUserId = 1L;
        return Result.success(activityPostService.updatePost(id, dto, imageFiles, videoFiles, currentUserId));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        activityPostService.deletePost(id);
        return Result.success();
    }

    @PostMapping("/{id}/like")
    public Result<Void> likePost(
            @PathVariable Long id,
            @RequestParam(required = false) Long elderId,
            HttpServletRequest request) {
        Long currentUserId = 1L;
        activityPostService.likePost(id, currentUserId, elderId);
        return Result.success();
    }
}
