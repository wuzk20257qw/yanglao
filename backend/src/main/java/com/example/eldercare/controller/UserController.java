package com.example.eldercare.controller;

import com.example.eldercare.common.PageResult;
import com.example.eldercare.common.Result;
import com.example.eldercare.dto.UserCreateDTO;
import com.example.eldercare.dto.UserUpdateDTO;
import com.example.eldercare.entity.User;
import com.example.eldercare.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Result<PageResult<User>> getUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status) {
        Page<User> userPage = userService.getUsers(page, size, username, realName, role, status);
        PageResult<User> result = PageResult.of(
                userPage.getContent(),
                userPage.getTotalElements(),
                userPage.getSize(),
                userPage.getNumber()
        );
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    @PostMapping
    public Result<User> createUser(@Valid @RequestBody UserCreateDTO dto) {
        User user = userService.createUser(dto);
        return Result.success("创建成功", user);
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        User user = userService.updateUser(id, dto);
        return Result.success("更新成功", user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<User> updateCurrentUser(@Valid @RequestBody UserUpdateDTO dto) {
        User user = userService.updateCurrentUser(dto);
        return Result.success("更新成功", user);
    }

    @GetMapping("/elders")
    public Result<List<User>> getRelatedElders() {
        List<User> elders = userService.getRelatedElders();
        return Result.success(elders);
    }

    @PostMapping("/settings")
    public Result<Void> updateSettings(@RequestBody Map<String, Object> settings) {
        userService.updateSettings(settings);
        return Result.success();
    }

    @PostMapping("/clear-cache")
    public Result<Void> clearCache() {
        userService.clearCache();
        return Result.success();
    }

    @PostMapping("/feedback")
    public Result<Void> submitFeedback(@RequestBody Map<String, Object> feedback) {
        userService.submitFeedback(feedback);
        return Result.success();
    }

    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount() {
        Long count = userService.getUnreadCount();
        return Result.success(count);
    }
}
