package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.NursingRecordDTO;
import com.example.eldercare.entity.NursingRecord;
import com.example.eldercare.service.NursingRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nursing-records")
@RequiredArgsConstructor
public class NursingRecordController {

    private final NursingRecordService nursingRecordService;

    @GetMapping
    public Result<PageResult<NursingRecord>> getNursingRecords(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String nursingType) {
        Page<NursingRecord> recordPage = nursingRecordService.getNursingRecords(page, size, elderId, nursingType);
        PageResult<NursingRecord> result = PageResult.of(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getSize(),
                recordPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<NursingRecord> getNursingRecordById(@PathVariable Long id) {
        NursingRecord record = nursingRecordService.getNursingRecordById(id);
        return Result.success(record);
    }

    @PostMapping
    public Result<NursingRecord> createNursingRecord(@Valid @RequestBody NursingRecordDTO dto) {
        NursingRecord record = nursingRecordService.createNursingRecord(dto);
        return Result.success("创建成功", record);
    }

    @PutMapping("/{id}")
    public Result<NursingRecord> updateNursingRecord(@PathVariable Long id, @Valid @RequestBody NursingRecordDTO dto) {
        NursingRecord record = nursingRecordService.updateNursingRecord(id, dto);
        return Result.success("更新成功", record);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNursingRecord(@PathVariable Long id) {
        nursingRecordService.deleteNursingRecord(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getNursingStatistics(
            @RequestParam Long elderId,
            @RequestParam(required = false) Integer days) {
        Map<String, Object> statistics = nursingRecordService.getNursingStatistics(elderId, days);
        return Result.success(statistics);
    }

    @PostMapping("/{id}/evaluation")
    public Result<Void> submitEvaluation(@PathVariable Long id, @RequestBody Map<String, Object> evaluation) {
        nursingRecordService.submitEvaluation(id, evaluation);
        return Result.success();
    }
}
