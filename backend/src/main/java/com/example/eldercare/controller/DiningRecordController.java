package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.DiningRecordDTO;
import com.example.eldercare.service.DiningRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dining-records")
@RequiredArgsConstructor
public class DiningRecordController {

    private final DiningRecordService diningRecordService;

    @GetMapping
    public Result<PageResult<DiningRecordDTO>> getDiningRecords(
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String mealType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DiningRecordDTO> result = diningRecordService.getDiningRecords(elderId, mealType, status, pageable);
        return Result.success(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<DiningRecordDTO> getDiningRecordById(@PathVariable Long id) {
        return Result.success(diningRecordService.getDiningRecordById(id));
    }

    @PostMapping
    public Result<DiningRecordDTO> createDiningRecord(@RequestBody DiningRecordDTO dto) {
        return Result.success(diningRecordService.createDiningRecord(dto));
    }

    @PutMapping("/{id}")
    public Result<DiningRecordDTO> updateDiningRecord(@PathVariable Long id, @RequestBody DiningRecordDTO dto) {
        return Result.success(diningRecordService.updateDiningRecord(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDiningRecord(@PathVariable Long id) {
        diningRecordService.deleteDiningRecord(id);
        return Result.success();
    }
}
