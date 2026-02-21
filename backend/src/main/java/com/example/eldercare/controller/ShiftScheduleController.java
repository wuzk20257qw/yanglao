package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ShiftScheduleDTO;
import com.example.eldercare.service.ShiftScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/shift-schedules")
@RequiredArgsConstructor
public class ShiftScheduleController {

    private final ShiftScheduleService shiftScheduleService;

    @GetMapping
    public Result<PageResult<ShiftScheduleDTO>> getShiftSchedules(
            @RequestParam(required = false) Long nurseId,
            @RequestParam(required = false) String shiftType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shiftDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ShiftScheduleDTO> result = shiftScheduleService.getShiftSchedules(nurseId, shiftType, shiftDate, status, pageable);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<ShiftScheduleDTO> getShiftScheduleById(@PathVariable Long id) {
        return Result.success(shiftScheduleService.getShiftScheduleById(id));
    }

    @PostMapping
    public Result<ShiftScheduleDTO> createShiftSchedule(@RequestBody ShiftScheduleDTO dto) {
        return Result.success(shiftScheduleService.createShiftSchedule(dto));
    }

    @PutMapping("/{id}")
    public Result<ShiftScheduleDTO> updateShiftSchedule(@PathVariable Long id, @RequestBody ShiftScheduleDTO dto) {
        return Result.success(shiftScheduleService.updateShiftSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteShiftSchedule(@PathVariable Long id) {
        shiftScheduleService.deleteShiftSchedule(id);
        return Result.success();
    }
}
