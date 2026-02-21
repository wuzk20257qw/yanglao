package com.example.eldercare.service;

import com.example.eldercare.dto.HealthRecordDTO;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.entity.HealthRecord;
import com.example.eldercare.repository.ElderRepository;
import com.example.eldercare.repository.HealthRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final ElderRepository elderRepository;

    @Transactional
    public HealthRecord createHealthRecord(HealthRecordDTO dto) {
        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        HealthRecord record = new HealthRecord();
        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setRecordType("常规体检");
        record.setBloodPressure(dto.getSystolicBP() + "/" + dto.getDiastolicBP());
        record.setHeartRate(dto.getHeartRate());
        record.setBloodSugar(dto.getBloodSugar());
        record.setBodyTemperature(dto.getTemperature());
        record.setRecordTime(dto.getRecordTime() != null ? dto.getRecordTime() : LocalDateTime.now());
        record.setNotes(dto.getRemark());

        return healthRecordRepository.save(record);
    }

    @Transactional
    public HealthRecord updateHealthRecord(Long id, HealthRecordDTO dto) {
        HealthRecord record = healthRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("健康记录不存在"));

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setRecordType("常规体检");
        record.setBloodPressure(dto.getSystolicBP() + "/" + dto.getDiastolicBP());
        record.setHeartRate(dto.getHeartRate());
        record.setBloodSugar(dto.getBloodSugar());
        record.setBodyTemperature(dto.getTemperature());
        record.setRecordTime(dto.getRecordTime() != null ? dto.getRecordTime() : LocalDateTime.now());
        record.setNotes(dto.getRemark());

        return healthRecordRepository.save(record);
    }

    @Transactional
    public void deleteHealthRecord(Long id) {
        HealthRecord record = healthRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("健康记录不存在"));
        healthRecordRepository.delete(record);
    }

    public HealthRecord getHealthRecordById(Long id) {
        return healthRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("健康记录不存在"));
    }

    public Page<HealthRecord> getHealthRecords(Integer page, Integer size, Long elderId, String recordType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordTime"));

        Specification<HealthRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (elderId != null) {
                predicates.add(cb.equal(root.get("elderId"), elderId));
            }
            if (recordType != null && !recordType.isEmpty()) {
                predicates.add(cb.equal(root.get("recordType"), recordType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return healthRecordRepository.findAll(spec, pageable);
    }

    public Map<String, Object> getHealthSummary(Long elderId, Integer days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<HealthRecord> records = healthRecordRepository.findByElderIdAndRecordTimeBetween(
                elderId, startDateTime, endDateTime);

        Map<String, Object> summary = new HashMap<>();
        summary.put("elderId", elderId);
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        summary.put("totalRecords", records.size());

        if (!records.isEmpty()) {
            double avgBP = records.stream()
                    .mapToDouble(r -> {
                        String[] bp = r.getBloodPressure().split("/");
                        if (bp.length == 2) {
                            return Double.parseDouble(bp[0]) + Double.parseDouble(bp[1]);
                        }
                        return 0;
                    }).average().orElse(0) / 2;

            double avgHeartRate = records.stream()
                    .mapToInt(r -> r.getHeartRate() != null ? r.getHeartRate() : 0)
                    .average().orElse(0);

            double avgBloodSugar = records.stream()
                    .mapToDouble(r -> r.getBloodSugar() != null ? r.getBloodSugar().doubleValue() : 0)
                    .average().orElse(0);

            double avgTemperature = records.stream()
                    .mapToDouble(r -> r.getBodyTemperature() != null ? r.getBodyTemperature().doubleValue() : 0)
                    .average().orElse(0);

            summary.put("avgBloodPressure", String.format("%.0f", avgBP));
            summary.put("avgHeartRate", String.format("%.0f", avgHeartRate));
            summary.put("avgBloodSugar", String.format("%.1f", avgBloodSugar));
            summary.put("avgTemperature", String.format("%.1f", avgTemperature));
        }

        return summary;
    }

    public List<Map<String, Object>> getHealthTrend(Long elderId, String indicator, Integer days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<HealthRecord> records = healthRecordRepository.findByElderIdAndRecordTimeBetweenOrderByRecordTime(
                elderId, startDateTime, endDateTime);

        return records.stream()
                .sorted(Comparator.comparing(HealthRecord::getRecordTime))
                .map(record -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", record.getRecordTime().toLocalDate().toString());
                    item.put("time", record.getRecordTime().toLocalTime().toString());

                    switch (indicator) {
                        case "bloodPressure":
                            String[] bp = record.getBloodPressure().split("/");
                            item.put("systolic", bp.length > 0 ? bp[0] : "0");
                            item.put("diastolic", bp.length > 1 ? bp[1] : "0");
                            break;
                        case "heartRate":
                            item.put("value", record.getHeartRate());
                            break;
                        case "bloodSugar":
                            item.put("value", record.getBloodSugar());
                            break;
                        case "temperature":
                            item.put("value", record.getBodyTemperature());
                            break;
                        default:
                            item.put("value", 0);
                    }
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRelatedNursingRecords(Long elderId, String startDate, String endDate) {
        LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(endDate).plusDays(1).atStartOfDay();

        // 获取健康异常记录的护理相关记录
        List<HealthRecord> records = healthRecordRepository.findByElderIdAndRecordTimeBetween(
                elderId, startDateTime, endDateTime);

        return records.stream()
                .map(record -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", record.getId());
                    item.put("type", "health");
                    item.put("time", record.getRecordTime().toString());
                    item.put("content", String.format("血压: %s, 心率: %d次/分",
                            record.getBloodPressure(),
                            record.getHeartRate()));
                    return item;
                })
                .collect(Collectors.toList());
    }
}
