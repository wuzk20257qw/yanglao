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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
