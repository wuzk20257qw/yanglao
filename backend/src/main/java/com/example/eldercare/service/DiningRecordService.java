package com.example.eldercare.service;

import com.example.eldercare.dto.DiningRecordDTO;
import com.example.eldercare.entity.DiningRecord;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.repository.DiningRecordRepository;
import com.example.eldercare.repository.ElderRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiningRecordService {

    private final DiningRecordRepository diningRecordRepository;
    private final ElderRepository elderRepository;

    public Page<DiningRecordDTO> getDiningRecords(Long elderId, String mealType, Integer status, Pageable pageable) {
        Specification<DiningRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (elderId != null) {
                predicates.add(cb.equal(root.get("elderId"), elderId));
            }
            if (mealType != null && !mealType.isEmpty()) {
                predicates.add(cb.equal(root.get("mealType"), mealType));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return diningRecordRepository.findAll(spec, pageable).map(this::toDTO);
    }

    public DiningRecordDTO getDiningRecordById(Long id) {
        DiningRecord record = diningRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐饮记录不存在"));
        return toDTO(record);
    }

    @Transactional
    public DiningRecordDTO createDiningRecord(DiningRecordDTO dto) {
        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new RuntimeException("老人不存在"));

        DiningRecord record = new DiningRecord();
        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setMealType(dto.getMealType());
        record.setMealName(dto.getMealName());
        record.setNutritionLevel(dto.getNutritionLevel());
        record.setNotes(dto.getNotes());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        record.setDiningDate(dto.getDiningDate());

        return toDTO(diningRecordRepository.save(record));
    }

    @Transactional
    public DiningRecordDTO updateDiningRecord(Long id, DiningRecordDTO dto) {
        DiningRecord record = diningRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐饮记录不存在"));

        if (dto.getMealType() != null) {
            record.setMealType(dto.getMealType());
        }
        if (dto.getMealName() != null) {
            record.setMealName(dto.getMealName());
        }
        if (dto.getNutritionLevel() != null) {
            record.setNutritionLevel(dto.getNutritionLevel());
        }
        if (dto.getNotes() != null) {
            record.setNotes(dto.getNotes());
        }
        if (dto.getStatus() != null) {
            record.setStatus(dto.getStatus());
        }
        if (dto.getDiningDate() != null) {
            record.setDiningDate(dto.getDiningDate());
        }

        return toDTO(diningRecordRepository.save(record));
    }

    @Transactional
    public void deleteDiningRecord(Long id) {
        diningRecordRepository.deleteById(id);
    }

    private DiningRecordDTO toDTO(DiningRecord record) {
        DiningRecordDTO dto = new DiningRecordDTO();
        dto.setId(record.getId());
        dto.setElderId(record.getElderId());
        dto.setElderName(record.getElderName());
        dto.setMealType(record.getMealType());
        dto.setMealName(record.getMealName());
        dto.setNutritionLevel(record.getNutritionLevel());
        dto.setNotes(record.getNotes());
        dto.setStatus(record.getStatus());
        dto.setDiningDate(record.getDiningDate());
        return dto;
    }

    public Map<String, Object> getMealPlan(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<DiningRecord> records = diningRecordRepository.findByDiningDateBetween(start, end);

        Map<String, Map<String, Object>> mealPlan = new HashMap<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String dateStr = date.toString();
            Map<String, Object> dayMeals = new HashMap<>();
            LocalDate currentDate = date;

            Map<String, List<DiningRecordDTO>> byType = records.stream()
                    .filter(r -> r.getDiningDate().equals(currentDate))
                    .map(this::toDTO)
                    .collect(Collectors.groupingBy(DiningRecordDTO::getMealType));

            dayMeals.put("breakfast", byType.getOrDefault("breakfast", Collections.emptyList()));
            dayMeals.put("lunch", byType.getOrDefault("lunch", Collections.emptyList()));
            dayMeals.put("dinner", byType.getOrDefault("dinner", Collections.emptyList()));

            mealPlan.put(dateStr, dayMeals);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("meals", mealPlan);
        return result;
    }

    @Transactional
    public void submitMealFeedback(Long id, Map<String, Object> feedback) {
        DiningRecord record = diningRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐饮记录不存在"));
        // 保存反馈逻辑
    }

    public Map<String, Object> getNutritionReport(Long elderId) {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        List<DiningRecord> records = diningRecordRepository.findByElderIdAndDiningDateBetween(
                elderId, start, end);

        Map<String, Object> report = new HashMap<>();
        report.put("elderId", elderId);
        report.put("totalMeals", records.size());

        Map<String, Long> mealCounts = records.stream()
                .collect(Collectors.groupingBy(DiningRecord::getMealType, Collectors.counting()));

        report.put("breakfastCount", mealCounts.getOrDefault("breakfast", 0L));
        report.put("lunchCount", mealCounts.getOrDefault("lunch", 0L));
        report.put("dinnerCount", mealCounts.getOrDefault("dinner", 0L));

        return report;
    }
}
