package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.AlarmRecordDTO;
import com.example.eldercare.entity.AlarmRecord;
import com.example.eldercare.service.AlarmRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alarm-records")
@RequiredArgsConstructor
public class AlarmRecordController {

    private final AlarmRecordService alarmRecordService;

    @GetMapping
    public Result<PageResult<AlarmRecord>> getAlarmRecords(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String alarmType,
            @RequestParam(required = false) Integer status) {
        Page<AlarmRecord> recordPage = alarmRecordService.getAlarmRecords(page, size, elderId, alarmType, status);
        PageResult<AlarmRecord> result = PageResult.of(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getSize(),
                recordPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<AlarmRecord> getAlarmRecordById(@PathVariable Long id) {
        AlarmRecord record = alarmRecordService.getAlarmRecordById(id);
        return Result.success(record);
    }

    @PostMapping
    public Result<AlarmRecord> createAlarmRecord(@Valid @RequestBody AlarmRecordDTO dto) {
        AlarmRecord record = alarmRecordService.createAlarmRecord(dto);
        return Result.success("创建成功", record);
    }

    @PutMapping("/{id}")
    public Result<AlarmRecord> updateAlarmRecord(@PathVariable Long id, @Valid @RequestBody AlarmRecordDTO dto) {
        AlarmRecord record = alarmRecordService.updateAlarmRecord(id, dto);
        return Result.success("更新成功", record);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteAlarmRecord(@PathVariable Long id) {
        alarmRecordService.deleteAlarmRecord(id);
        return Result.success();
    }

    @PostMapping("/{id}/handle")
    public Result<AlarmRecord> handleAlarm(
            @PathVariable Long id,
            @RequestParam String handleBy,
            @RequestParam(required = false) String handleNotes) {
        AlarmRecord record = alarmRecordService.handleAlarm(id, handleBy, handleNotes);
        return Result.success("处理成功", record);
    }
}
