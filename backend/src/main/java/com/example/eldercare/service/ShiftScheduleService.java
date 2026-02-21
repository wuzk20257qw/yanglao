package com.example.eldercare.service;

import com.example.eldercare.dto.ShiftScheduleDTO;
import com.example.eldercare.entity.ShiftSchedule;
import com.example.eldercare.entity.User;
import com.example.eldercare.repository.ShiftScheduleRepository;
import com.example.eldercare.repository.UserRepository;
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
public class ShiftScheduleService {

    private final ShiftScheduleRepository shiftScheduleRepository;
    private final UserRepository userRepository;

    public Page<ShiftScheduleDTO> getShiftSchedules(Long nurseId, String shiftType, LocalDate shiftDate, Integer status, Pageable pageable) {
        Specification<ShiftSchedule> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (nurseId != null) {
                predicates.add(cb.equal(root.get("nurseId"), nurseId));
            }
            if (shiftType != null && !shiftType.isEmpty()) {
                predicates.add(cb.equal(root.get("shiftType"), shiftType));
            }
            if (shiftDate != null) {
                predicates.add(cb.equal(root.get("shiftDate"), shiftDate));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return shiftScheduleRepository.findAll(spec, pageable).map(this::toDTO);
    }

    public ShiftScheduleDTO getShiftScheduleById(Long id) {
        ShiftSchedule schedule = shiftScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("排班记录不存在"));
        return toDTO(schedule);
    }

    @Transactional
    public ShiftScheduleDTO createShiftSchedule(ShiftScheduleDTO dto) {
        User nurse = userRepository.findById(dto.getNurseId())
                .orElseThrow(() -> new RuntimeException("护士不存在"));

        ShiftSchedule schedule = new ShiftSchedule();
        schedule.setNurseId(dto.getNurseId());
        schedule.setNurseName(nurse.getUsername());
        schedule.setShiftType(dto.getShiftType());
        schedule.setShiftDate(dto.getShiftDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setNotes(dto.getNotes());
        schedule.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        return toDTO(shiftScheduleRepository.save(schedule));
    }

    @Transactional
    public ShiftScheduleDTO updateShiftSchedule(Long id, ShiftScheduleDTO dto) {
        ShiftSchedule schedule = shiftScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("排班记录不存在"));

        if (dto.getShiftType() != null) {
            schedule.setShiftType(dto.getShiftType());
        }
        if (dto.getShiftDate() != null) {
            schedule.setShiftDate(dto.getShiftDate());
        }
        if (dto.getStartTime() != null) {
            schedule.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            schedule.setEndTime(dto.getEndTime());
        }
        if (dto.getNotes() != null) {
            schedule.setNotes(dto.getNotes());
        }
        if (dto.getStatus() != null) {
            schedule.setStatus(dto.getStatus());
        }

        return toDTO(shiftScheduleRepository.save(schedule));
    }

    @Transactional
    public void deleteShiftSchedule(Long id) {
        shiftScheduleRepository.deleteById(id);
    }

    private ShiftScheduleDTO toDTO(ShiftSchedule schedule) {
        ShiftScheduleDTO dto = new ShiftScheduleDTO();
        dto.setId(schedule.getId());
        dto.setNurseId(schedule.getNurseId());
        dto.setNurseName(schedule.getNurseName());
        dto.setShiftType(schedule.getShiftType());
        dto.setShiftDate(schedule.getShiftDate());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setNotes(schedule.getNotes());
        dto.setStatus(schedule.getStatus());
        return dto;
    }
}
