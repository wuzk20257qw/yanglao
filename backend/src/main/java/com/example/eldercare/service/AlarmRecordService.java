package com.example.eldercare.service;

import com.example.eldercare.dto.AlarmRecordDTO;
import com.example.eldercare.entity.AlarmRecord;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.repository.AlarmRecordRepository;
import com.example.eldercare.repository.ElderRepository;
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
public class AlarmRecordService {

    private final AlarmRecordRepository alarmRecordRepository;
    private final ElderRepository elderRepository;

    @Transactional
    public AlarmRecord createAlarmRecord(AlarmRecordDTO dto) {
        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        AlarmRecord record = new AlarmRecord();
        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setElderPhone(elder.getPhone());
        record.setAlarmType(dto.getAlarmType());
        record.setAlarmLevel(dto.getAlarmLevel());
        record.setContent(dto.getContent());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);

        return alarmRecordRepository.save(record);
    }

    @Transactional
    public AlarmRecord updateAlarmRecord(Long id, AlarmRecordDTO dto) {
        AlarmRecord record = alarmRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("告警记录不存在"));

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setElderPhone(elder.getPhone());
        record.setAlarmType(dto.getAlarmType());
        record.setAlarmLevel(dto.getAlarmLevel());
        record.setContent(dto.getContent());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : record.getStatus());
        record.setHandleBy(dto.getHandleBy());
        record.setHandleNotes(dto.getHandleNotes());
        record.setHandleTime(dto.getHandleTime());

        return alarmRecordRepository.save(record);
    }

    @Transactional
    public void deleteAlarmRecord(Long id) {
        AlarmRecord record = alarmRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("告警记录不存在"));
        alarmRecordRepository.delete(record);
    }

    public AlarmRecord getAlarmRecordById(Long id) {
        return alarmRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("告警记录不存在"));
    }

    public Page<AlarmRecord> getAlarmRecords(Integer page, Integer size, Long elderId, String alarmType, Integer status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<AlarmRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (elderId != null) {
                predicates.add(cb.equal(root.get("elderId"), elderId));
            }
            if (alarmType != null && !alarmType.isEmpty()) {
                predicates.add(cb.equal(root.get("alarmType"), alarmType));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return alarmRecordRepository.findAll(spec, pageable);
    }

    @Transactional
    public AlarmRecord handleAlarm(Long id, String handleBy, String handleNotes) {
        AlarmRecord record = alarmRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("告警记录不存在"));

        record.setStatus(1);
        record.setHandleBy(handleBy);
        record.setHandleNotes(handleNotes);
        record.setHandleTime(LocalDateTime.now());

        return alarmRecordRepository.save(record);
    }
}
