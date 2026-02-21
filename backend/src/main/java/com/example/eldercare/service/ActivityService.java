package com.example.eldercare.service;

import com.example.eldercare.dto.ActivityDTO;
import com.example.eldercare.entity.Activity;
import com.example.eldercare.repository.ActivityRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Page<ActivityDTO> getActivities(String activityType, LocalDate activityDate, Integer status, Pageable pageable) {
        Specification<Activity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (activityType != null && !activityType.isEmpty()) {
                predicates.add(cb.equal(root.get("activityType"), activityType));
            }
            if (activityDate != null) {
                predicates.add(cb.equal(root.get("activityDate"), activityDate));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return activityRepository.findAll(spec, pageable).map(this::toDTO);
    }

    public ActivityDTO getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        return toDTO(activity);
    }

    @Transactional
    public ActivityDTO createActivity(ActivityDTO dto) {
        Activity activity = new Activity();
        activity.setName(dto.getName());
        activity.setDescription(dto.getDescription());
        activity.setActivityType(dto.getActivityType());
        activity.setActivityDate(dto.getActivityDate());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setLocation(dto.getLocation());
        activity.setMaxParticipants(dto.getMaxParticipants());
        activity.setCurrentParticipants(0);
        activity.setFeeAmount(dto.getFeeAmount() != null ? dto.getFeeAmount() : 0.0);
        activity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        return toDTO(activityRepository.save(activity));
    }

    @Transactional
    public ActivityDTO updateActivity(Long id, ActivityDTO dto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("活动不存在"));

        if (dto.getName() != null) {
            activity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            activity.setDescription(dto.getDescription());
        }
        if (dto.getActivityType() != null) {
            activity.setActivityType(dto.getActivityType());
        }
        if (dto.getActivityDate() != null) {
            activity.setActivityDate(dto.getActivityDate());
        }
        if (dto.getStartTime() != null) {
            activity.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            activity.setEndTime(dto.getEndTime());
        }
        if (dto.getLocation() != null) {
            activity.setLocation(dto.getLocation());
        }
        if (dto.getMaxParticipants() != null) {
            activity.setMaxParticipants(dto.getMaxParticipants());
        }
        if (dto.getFeeAmount() != null) {
            activity.setFeeAmount(dto.getFeeAmount());
        }
        if (dto.getStatus() != null) {
            activity.setStatus(dto.getStatus());
        }

        return toDTO(activityRepository.save(activity));
    }

    @Transactional
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    @Transactional
    public void incrementParticipants(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        activityRepository.save(activity);
    }

    @Transactional
    public void decrementParticipants(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("活动不存在"));
        if (activity.getCurrentParticipants() > 0) {
            activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
            activityRepository.save(activity);
        }
    }

    private ActivityDTO toDTO(Activity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setDescription(activity.getDescription());
        dto.setActivityType(activity.getActivityType());
        dto.setActivityDate(activity.getActivityDate());
        dto.setStartTime(activity.getStartTime());
        dto.setEndTime(activity.getEndTime());
        dto.setLocation(activity.getLocation());
        dto.setMaxParticipants(activity.getMaxParticipants());
        dto.setCurrentParticipants(activity.getCurrentParticipants());
        dto.setFeeAmount(activity.getFeeAmount());
        dto.setStatus(activity.getStatus());
        return dto;
    }

    @Transactional
    public void joinActivity(Long activityId) {
        incrementParticipants(activityId);
    }

    @Transactional
    public void cancelJoin(Long activityId) {
        decrementParticipants(activityId);
    }

    public List<ActivityDTO> getJoinedActivities(Long userId) {
        return activityRepository.findAll().stream()
                .limit(10)
                .map(this::toDTO)
                .toList();
    }
}
