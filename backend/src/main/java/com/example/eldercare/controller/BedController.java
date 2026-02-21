package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.BedDTO;
import com.example.eldercare.entity.Bed;
import com.example.eldercare.service.BedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;

    @GetMapping
    public Result<PageResult<Bed>> getBeds(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String building,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status) {
        Page<Bed> bedPage = bedService.getBeds(page, size, building, floor, type, status);
        PageResult<Bed> result = PageResult.of(
                bedPage.getContent(),
                bedPage.getTotalElements(),
                bedPage.getSize(),
                bedPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Bed> getBedById(@PathVariable Long id) {
        Bed bed = bedService.getBedById(id);
        return Result.success(bed);
    }

    @PostMapping
    public Result<Bed> createBed(@Valid @RequestBody BedDTO dto) {
        Bed bed = bedService.createBed(dto);
        return Result.success("创建成功", bed);
    }

    @PutMapping("/{id}")
    public Result<Bed> updateBed(@PathVariable Long id, @Valid @RequestBody BedDTO dto) {
        Bed bed = bedService.updateBed(id, dto);
        return Result.success("更新成功", bed);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBed(@PathVariable Long id) {
        bedService.deleteBed(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Bed> updateBedStatus(@PathVariable Long id, @RequestParam Integer status) {
        Bed bed = bedService.updateBedStatus(id, status);
        return Result.success("状态更新成功", bed);
    }
}
