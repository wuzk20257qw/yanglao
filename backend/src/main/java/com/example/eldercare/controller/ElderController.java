package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ElderCreateDTO;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.service.ElderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/elders")
@RequiredArgsConstructor
public class ElderController {

    private final ElderService elderService;

    @GetMapping
    public Result<PageResult<Elder>> getElders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nursingLevel,
            @RequestParam(required = false) Integer status) {
        Page<Elder> elderPage = elderService.getElders(page, size, name, nursingLevel, status);
        PageResult<Elder> result = PageResult.of(
                elderPage.getContent(),
                elderPage.getTotalElements(),
                elderPage.getSize(),
                elderPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Elder> getElderById(@PathVariable Long id) {
        Elder elder = elderService.getElderById(id);
        return Result.success(elder);
    }

    @PostMapping
    public Result<Elder> createElder(@Valid @RequestBody ElderCreateDTO dto) {
        Elder elder = elderService.createElder(dto);
        return Result.success("创建成功", elder);
    }

    @PutMapping("/{id}")
    public Result<Elder> updateElder(@PathVariable Long id, @Valid @RequestBody ElderCreateDTO dto) {
        Elder elder = elderService.updateElder(id, dto);
        return Result.success("更新成功", elder);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteElder(@PathVariable Long id) {
        elderService.deleteElder(id);
        return Result.success();
    }

    @PostMapping("/{elderId}/assign-bed")
    public Result<Elder> assignBed(@PathVariable Long elderId, @RequestParam Long bedId) {
        Elder elder = elderService.assignBed(elderId, bedId);
        return Result.success("床位分配成功", elder);
    }

    @PostMapping("/{elderId}/release-bed")
    public Result<Elder> releaseBed(@PathVariable Long elderId) {
        Elder elder = elderService.releaseBed(elderId);
        return Result.success("床位释放成功", elder);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = elderService.getElderStats();
        return Result.success(stats);
    }
}
