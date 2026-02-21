package com.example.eldercare.service;

import com.example.eldercare.dto.ChangePasswordDTO;
import com.example.eldercare.dto.LoginDTO;
import com.example.eldercare.dto.UserCreateDTO;
import com.example.eldercare.dto.UserUpdateDTO;
import com.example.eldercare.entity.User;
import com.example.eldercare.repository.UserRepository;
import com.example.eldercare.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, Object> login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new com.example.eldercare.common.BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new com.example.eldercare.common.BusinessException("账号已被禁用");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new com.example.eldercare.common.BusinessException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", getUserInfo(user));
        return result;
    }

    private Map<String, Object> getUserInfo(User user) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("realName", user.getRealName());
        info.put("phone", user.getPhone());
        info.put("role", user.getRole());
        return info;
    }

    @Transactional
    public User createUser(UserCreateDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new com.example.eldercare.common.BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setDeptId(dto.getDeptId());
        user.setStatus(1);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setDeptId(dto.getDeptId());
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        user.setStatus(0);
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
    }

    public Page<User> getUsers(Integer page, Integer size, String username, String realName, String role, Integer status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            if (realName != null && !realName.isEmpty()) {
                predicates.add(cb.like(root.get("realName"), "%" + realName + "%"));
            }
            if (role != null && !role.isEmpty()) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new com.example.eldercare.common.BusinessException("原密码不正确");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new com.example.eldercare.common.BusinessException("两次新密码不一致");
        }

        if (dto.getNewPassword().equals(dto.getOldPassword())) {
            throw new com.example.eldercare.common.BusinessException("新密码不能与原密码相同");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        log.info("用户{}修改密码成功", user.getUsername());
    }

    public User validateTokenAndGetUser(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new com.example.eldercare.common.BusinessException(401, "Token无效或已过期");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));
    }
}
