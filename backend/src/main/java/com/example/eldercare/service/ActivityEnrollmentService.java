package com.example.eldercare.service;

import com.example.eldercare.dto.ActivityEnrollmentDTO;
import com.example.eldercare.entity.Activity;
import com.example.eldercare.entity.ActivityEnrollment;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.repository.ActivityEnrollmentRepository;
import com.example.eldercare.repository.ActivityRepository;
import com.example.eldercare.repository.ElderRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityEnrollmentService {

    private final ActivityEnrollmentRepository enrollmentRepository;
    private final ActivityRepository activityRepository;
    private final ElderRepository elderRepository;
    private final ActivityService activityService;

    public Page<ActivityEnrollmentDTO> getEnrollments(Long activityId, Long elderId, Integer status, Pageable pageable) {
        Specification<ActivityEnrollment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (activityId != null) {
                predicates.add(cb.equal(root.get("activityId"), activityId));
            }
            if (elderId != null) {
                predicates.add(cb.equal(root.get("elderId"), elderId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return enrollmentRepository.findAll(spec, pageable).map(this::toDTO);
    }

    public ActivityEnrollmentDTO getEnrollmentById(Long id) {
        ActivityEnrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("报名记录不存在"));
        return toDTO(enrollment);
    }

    @Transactional
    public ActivityEnrollmentDTO enroll(ActivityEnrollmentDTO dto) {
        Activity activity = activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new RuntimeException("活动不存在"));

        if (activity.getMaxParticipants() != null &&
            activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new RuntimeException("活动报名人数已满");
        }

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new RuntimeException("老人不存在"));

        ActivityEnrollment enrollment = new ActivityEnrollment();
        enrollment.setActivityId(dto.getActivityId());
        enrollment.setActivityName(activity.getName());
        enrollment.setElderId(dto.getElderId());
        enrollment.setElderName(elder.getName());
        enrollment.setEnrollmentTime(dto.getEnrollmentTime() != null ? dto.getEnrollmentTime() : LocalDateTime.now());
        enrollment.setNotes(dto.getNotes());
        enrollment.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        ActivityEnrollment saved = enrollmentRepository.save(enrollment);
        activityService.incrementParticipants(dto.getActivityId());

        return toDTO(saved);
    }

    @Transactional
    public ActivityEnrollmentDTO cancelEnrollment(Long id) {
        ActivityEnrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("报名记录不存在"));

        enrollment.setStatus(0);
        ActivityEnrollment saved = enrollmentRepository.save(enrollment);
        activityService.decrementParticipants(enrollment.getActivityId());

        return toDTO(saved);
    }

    @Transactional
    public void deleteEnrollment(Long id) {
        ActivityEnrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("报名记录不存在"));

        if (enrollment.getStatus() == 1) {
            activityService.decrementParticipants(enrollment.getActivityId());
        }

        enrollmentRepository.deleteById(id);
    }

    private ActivityEnrollmentDTO toDTO(ActivityEnrollment enrollment) {
        ActivityEnrollmentDTO dto = new ActivityEnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setActivityId(enrollment.getActivityId());
        dto.setActivityName(enrollment.getActivityName());
        dto.setElderId(enrollment.getElderId());
        dto.setElderName(enrollment.getElderName());
        dto.setEnrollmentTime(enrollment.getEnrollmentTime());
        dto.setNotes(enrollment.getNotes());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }
}
