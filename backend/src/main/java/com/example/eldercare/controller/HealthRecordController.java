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
}
