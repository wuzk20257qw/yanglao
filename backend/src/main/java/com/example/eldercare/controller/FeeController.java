package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.FeeRecordDTO;
import com.example.eldercare.entity.FeeRecord;
import com.example.eldercare.entity.FeeType;
import com.example.eldercare.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @GetMapping
    public Result<PageResult<FeeRecord>> getFeeRecords(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long elderId,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) Integer status) {
        Page<FeeRecord> recordPage = feeService.getFeeRecords(page, size, elderId, feeType, status);
        PageResult<FeeRecord> result = PageResult.of(
                recordPage.getContent(),
                recordPage.getTotalElements(),
                recordPage.getSize(),
                recordPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<FeeRecord> getFeeRecordById(@PathVariable Long id) {
        FeeRecord record = feeService.getFeeRecordById(id);
        return Result.success(record);
    }

    @PostMapping
    public Result<FeeRecord> createFeeRecord(@Valid @RequestBody FeeRecordDTO dto) {
        FeeRecord record = feeService.createFeeRecord(dto);
        return Result.success("创建成功", record);
    }

    @PutMapping("/{id}")
    public Result<FeeRecord> updateFeeRecord(@PathVariable Long id, @Valid @RequestBody FeeRecordDTO dto) {
        FeeRecord record = feeService.updateFeeRecord(id, dto);
        return Result.success("更新成功", record);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteFeeRecord(@PathVariable Long id) {
        feeService.deleteFeeRecord(id);
        return Result.success();
    }

    @PostMapping("/{id}/pay")
    public Result<FeeRecord> payFee(@PathVariable Long id) {
        FeeRecord record = feeService.payFee(id);
        return Result.success("缴费成功", record);
    }

    @GetMapping("/elder/{elderId}/total")
    public Result<BigDecimal> getTotalFeeByElderId(@PathVariable Long elderId) {
        BigDecimal total = feeService.getTotalFeeByElderId(elderId);
        return Result.success(total);
    }

    @GetMapping("/fee-types")
    public Result<List<FeeType>> getAllFeeTypes() {
        List<FeeType> feeTypes = feeService.getAllFeeTypes();
        return Result.success(feeTypes);
    }
}
