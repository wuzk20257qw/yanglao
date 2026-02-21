package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ActivityEnrollmentDTO;
import com.example.eldercare.service.ActivityEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity-enrollments")
@RequiredArgsConstructor
public class ActivityEnrollmentController {

    private final ActivityEnrollmentService enrollmentService;

    @GetMapping
    public Result<PageResult<ActivityEnrollmentDTO>> getEnrollments(
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityEnrollmentDTO> result = enrollmentService.getEnrollments(activityId, elderId, status, pageable);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<ActivityEnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        return Result.success(enrollmentService.getEnrollmentById(id));
    }

    @PostMapping
    public Result<ActivityEnrollmentDTO> enroll(@RequestBody ActivityEnrollmentDTO dto) {
        return Result.success(enrollmentService.enroll(dto));
    }

    @PutMapping("/{id}/cancel")
    public Result<ActivityEnrollmentDTO> cancelEnrollment(@PathVariable Long id) {
        return Result.success(enrollmentService.cancelEnrollment(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return Result.success();
    }
}
