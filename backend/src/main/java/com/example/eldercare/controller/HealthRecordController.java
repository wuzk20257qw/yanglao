package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.HealthRecordDTO;
import com.example.eldercare.entity.HealthRecord;
import com.example.eldercare.service.HealthRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health-records")
@RequiredArgsConstructor
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @GetMapping
    public Result<PageResult<HealthRecord>> getHealthRecords(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String recordType) {
        Page<HealthRecord> recordPage = healthRecordService.getHealthRecords(page, size, elderId, recordType);
        PageResult<HealthRecord> result = PageResult.of(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getSize(),
                recordPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<HealthRecord> getHealthRecordById(@PathVariable Long id) {
        HealthRecord record = healthRecordService.getHealthRecordById(id);
        return Result.success(record);
    }

    @PostMapping
    public Result<HealthRecord> createHealthRecord(@Valid @RequestBody HealthRecordDTO dto) {
        HealthRecord record = healthRecordService.createHealthRecord(dto);
        return Result.success("创建成功", record);
    }

    @PutMapping("/{id}")
    public Result<HealthRecord> updateHealthRecord(@PathVariable Long id, @Valid @RequestBody HealthRecordDTO dto) {
        HealthRecord record = healthRecordService.updateHealthRecord(id, dto);
        return Result.success("更新成功", record);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteHealthRecord(@PathVariable Long id) {
        healthRecordService.deleteHealthRecord(id);
        return Result.success();
    }

    @GetMapping("/elder/{elderId}/summary")
    public Result<Map<String, Object>> getHealthSummary(
            @PathVariable Long elderId,
            @RequestParam(defaultValue = "7") Integer days) {
        Map<String, Object> summary = healthRecordService.getHealthSummary(elderId, days);
        return Result.success(summary);
    }

    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> getHealthTrend(
            @RequestParam Long elderId,
            @RequestParam String indicator,
            @RequestParam(defaultValue = "7") Integer days) {
        List<Map<String, Object>> trend = healthRecordService.getHealthTrend(elderId, indicator, days);
        return Result.success(trend);
    }

    @GetMapping("/related/nursing")
    public Result<List<Map<String, Object>>> getRelatedNursingRecords(
            @RequestParam Long elderId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<Map<String, Object>> records = healthRecordService.getRelatedNursingRecords(elderId, startDate, endDate);
        return Result.success(records);
    }
}
