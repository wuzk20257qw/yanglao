package com.example.eldercare.config;

import com.example.eldercare.common.BusinessException;
import com.example.eldercare.entity.User;
import com.example.eldercare.service.UserService;
import com.example.eldercare.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api/auth/")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(401, "未提供认证Token");
        }

        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "Token无效或已过期");
        }

        User user = userService.validateTokenAndGetUser(token);
        request.setAttribute("currentUser", user);

        return true;
    }
}
