package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ActivityDTO;
import com.example.eldercare.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public Result<PageResult<ActivityDTO>> getActivities(
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate activityDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityDTO> result = activityService.getActivities(activityType, activityDate, status, pageable);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<ActivityDTO> getActivityById(@PathVariable Long id) {
        return Result.success(activityService.getActivityById(id));
    }

    @PostMapping
    public Result<ActivityDTO> createActivity(@RequestBody ActivityDTO dto) {
        return Result.success(activityService.createActivity(dto));
    }

    @PutMapping("/{id}")
    public Result<ActivityDTO> updateActivity(@PathVariable Long id, @RequestBody ActivityDTO dto) {
        return Result.success(activityService.updateActivity(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return Result.success();
    }

    @PostMapping("/{id}/join")
    public Result<Void> joinActivity(@PathVariable Long id) {
        activityService.joinActivity(id);
        return Result.success();
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancelJoin(@PathVariable Long id) {
        activityService.cancelJoin(id);
        return Result.success();
    }

    @GetMapping("/joined")
    public Result<List<ActivityDTO>> getJoinedActivities() {
        Long userId = 1L;
        List<ActivityDTO> activities = activityService.getJoinedActivities(userId);
        return Result.success(activities);
    }
}
