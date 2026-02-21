package com.example.eldercare.controller;

import com.example.eldercare.common.Result;
import com.example.eldercare.dto.ChangePasswordDTO;
import com.example.eldercare.dto.LoginDTO;
import com.example.eldercare.entity.User;
import com.example.eldercare.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success(result);
    }

    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestBody ChangePasswordDTO dto, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        userService.changePassword(currentUser.getId(), dto);
        return Result.success();
    }

    @GetMapping("/current-user")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        return Result.success(currentUser);
    }
}
