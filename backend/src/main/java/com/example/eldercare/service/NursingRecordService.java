package com.example.eldercare.service;

import com.example.eldercare.dto.NursingRecordDTO;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.entity.NursingRecord;
import com.example.eldercare.repository.ElderRepository;
import com.example.eldercare.repository.NursingRecordRepository;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NursingRecordService {

    private final NursingRecordRepository nursingRecordRepository;
    private final ElderRepository elderRepository;

    @Transactional
    public NursingRecord createNursingRecord(NursingRecordDTO dto) {
        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        NursingRecord record = new NursingRecord();
        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setNursingType(dto.getNursingType());
        record.setNursingLevel(dto.getNursingLevel());
        record.setContent(dto.getContent());
        record.setNotes(dto.getNotes());
        record.setRecordDate(dto.getRecordDate() != null ? dto.getRecordDate() : LocalDate.now());
        record.setRecordTime(dto.getRecordTime() != null ? dto.getRecordTime() : LocalTime.now());

        return nursingRecordRepository.save(record);
    }

    @Transactional
    public NursingRecord updateNursingRecord(Long id, NursingRecordDTO dto) {
        NursingRecord record = nursingRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("护理记录不存在"));

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setNursingType(dto.getNursingType());
        record.setNursingLevel(dto.getNursingLevel());
        record.setContent(dto.getContent());
        record.setNotes(dto.getNotes());
        record.setRecordDate(dto.getRecordDate() != null ? dto.getRecordDate() : LocalDate.now());
        record.setRecordTime(dto.getRecordTime() != null ? dto.getRecordTime() : LocalTime.now());

        return nursingRecordRepository.save(record);
    }

    @Transactional
    public void deleteNursingRecord(Long id) {
        NursingRecord record = nursingRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("护理记录不存在"));
        nursingRecordRepository.delete(record);
    }

    public NursingRecord getNursingRecordById(Long id) {
        return nursingRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("护理记录不存在"));
    }

    public Page<NursingRecord> getNursingRecords(Integer page, Integer size, Long elderId, String nursingType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recordDate", "recordTime"));

        Specification<NursingRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (elderId != null) {
                predicates.add(cb.equal(root.get("elderId"), elderId));
            }
            if (nursingType != null && !nursingType.isEmpty()) {
                predicates.add(cb.equal(root.get("nursingType"), nursingType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return nursingRecordRepository.findAll(spec, pageable);
    }
}
