package com.example.eldercare.service;

import com.example.eldercare.dto.FeeRecordDTO;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.entity.FeeRecord;
import com.example.eldercare.entity.FeeType;
import com.example.eldercare.repository.ElderRepository;
import com.example.eldercare.repository.FeeRecordRepository;
import com.example.eldercare.repository.FeeTypeRepository;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeeService {

    private final FeeRecordRepository feeRecordRepository;
    private final ElderRepository elderRepository;
    private final FeeTypeRepository feeTypeRepository;

    @Transactional
    public FeeRecord createFeeRecord(FeeRecordDTO dto) {
        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        FeeRecord record = new FeeRecord();
        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setFeeType(dto.getFeeType());
        record.setFeeName(dto.getFeeType());
        record.setAmount(dto.getAmount());
        record.setFeeDate(dto.getFeeDate() != null ? dto.getFeeDate() : LocalDate.now());
        record.setPeriod(dto.getPeriod());
        record.setNotes(dto.getNotes());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);

        return feeRecordRepository.save(record);
    }

    @Transactional
    public FeeRecord updateFeeRecord(Long id, FeeRecordDTO dto) {
        FeeRecord record = feeRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("费用记录不存在"));

        Elder elder = elderRepository.findById(dto.getElderId())
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        record.setElderId(dto.getElderId());
        record.setElderName(elder.getName());
        record.setFeeType(dto.getFeeType());
        record.setFeeName(dto.getFeeType());
        record.setAmount(dto.getAmount());
        record.setFeeDate(dto.getFeeDate() != null ? dto.getFeeDate() : LocalDate.now());
        record.setPeriod(dto.getPeriod());
        record.setNotes(dto.getNotes());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : record.getStatus());

        return feeRecordRepository.save(record);
    }

    @Transactional
    public void deleteFeeRecord(Long id) {
        FeeRecord record = feeRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("费用记录不存在"));
        feeRecordRepository.delete(record);
    }

    public FeeRecord getFeeRecordById(Long id) {
        return feeRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("费用记录不存在"));
    }

    public Page<FeeRecord> getFeeRecords(Integer page, Integer size, Long elderId, String feeType, Integer status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "feeDate"));

        Specification<FeeRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (elderId != null) {
                predicates.add(cb.equal(root.get("elderId"), elderId));
            }
            if (feeType != null && !feeType.isEmpty()) {
                predicates.add(cb.equal(root.get("feeType"), feeType));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return feeRecordRepository.findAll(spec, pageable);
    }

    @Transactional
    public FeeRecord payFee(Long id) {
        FeeRecord record = feeRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("费用记录不存在"));

        record.setStatus(1);
        record.setPayTime(java.time.LocalDateTime.now());

        return feeRecordRepository.save(record);
    }

    public BigDecimal getTotalFeeByElderId(Long elderId) {
        List<FeeRecord> records = feeRecordRepository.findByElderId(elderId);
        return records.stream()
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<FeeType> getAllFeeTypes() {
        return feeTypeRepository.findAll();
    }

    public Map<String, Object> getFeeSummary() {
        Long userId = 1L;
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        List<FeeRecord> allFees = feeRecordRepository.findAll();
        List<FeeRecord> monthlyFees = allFees.stream()
                .filter(f -> !f.getFeeDate().isBefore(startOfMonth) && !f.getFeeDate().isAfter(endOfMonth))
                .toList();

        BigDecimal totalAmount = monthlyFees.stream()
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paidAmount = monthlyFees.stream()
                .filter(f -> f.getStatus() == 1)
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal unpaidAmount = totalAmount.subtract(paidAmount);

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAmount", totalAmount);
        summary.put("paidAmount", paidAmount);
        summary.put("unpaidAmount", unpaidAmount);
        summary.put("month", currentMonth.toString());

        return summary;
    }

    public Page<FeeRecord> getPaymentHistory(Integer page, Integer size) {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "payTime"));

        Specification<FeeRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), 1));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return feeRecordRepository.findAll(spec, pageable);
    }
}
