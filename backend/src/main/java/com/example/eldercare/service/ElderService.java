package com.example.eldercare.service;

import com.example.eldercare.dto.ElderCreateDTO;
import com.example.eldercare.entity.Bed;
import com.example.eldercare.entity.Elder;
import com.example.eldercare.repository.BedRepository;
import com.example.eldercare.repository.ElderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElderService {

    private final ElderRepository elderRepository;
    private final BedRepository bedRepository;

    @Transactional
    public Elder createElder(ElderCreateDTO dto) {
        if (dto.getIdCard() != null && elderRepository.existsByIdCard(dto.getIdCard())) {
            throw new com.example.eldercare.common.BusinessException("身份证号已存在");
        }

        Elder elder = new Elder();
        elder.setName(dto.getName());
        elder.setGender(dto.getGender());
        elder.setBirthDate(dto.getBirthDate());
        elder.setIdCard(dto.getIdCard());
        elder.setPhone(dto.getPhone());
        elder.setAddress(dto.getAddress());
        elder.setNursingLevel(dto.getNursingLevel());
        elder.setAdmissionDate(dto.getAdmissionDate());
        elder.setEmergencyContact(dto.getEmergencyContact());
        elder.setEmergencyPhone(dto.getEmergencyPhone());
        elder.setHealthStatus(dto.getHealthStatus());
        elder.setAllergies(dto.getAllergies());
        elder.setStatus(1);

        return elderRepository.save(elder);
    }

    @Transactional
    public Elder updateElder(Long id, ElderCreateDTO dto) {
        Elder elder = elderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        if (dto.getIdCard() != null && !dto.getIdCard().equals(elder.getIdCard()) &&
            elderRepository.existsByIdCard(dto.getIdCard())) {
            throw new com.example.eldercare.common.BusinessException("身份证号已存在");
        }

        elder.setName(dto.getName());
        elder.setGender(dto.getGender());
        elder.setBirthDate(dto.getBirthDate());
        elder.setIdCard(dto.getIdCard());
        elder.setPhone(dto.getPhone());
        elder.setAddress(dto.getAddress());
        elder.setNursingLevel(dto.getNursingLevel());
        elder.setAdmissionDate(dto.getAdmissionDate());
        elder.setEmergencyContact(dto.getEmergencyContact());
        elder.setEmergencyPhone(dto.getEmergencyPhone());
        elder.setHealthStatus(dto.getHealthStatus());
        elder.setAllergies(dto.getAllergies());

        return elderRepository.save(elder);
    }

    @Transactional
    public void deleteElder(Long id) {
        Elder elder = elderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        elder.setStatus(2);
        elderRepository.save(elder);
    }

    public Elder getElderById(Long id) {
        return elderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));
    }

    public Page<Elder> getElders(Integer page, Integer size, String name, String nursingLevel, Integer status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<Elder> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (nursingLevel != null && !nursingLevel.isEmpty()) {
                predicates.add(cb.equal(root.get("nursingLevel"), nursingLevel));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return elderRepository.findAll(spec, pageable);
    }

    @Transactional
    public Elder assignBed(Long elderId, Long bedId) {
        Elder elder = elderRepository.findById(elderId)
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        if (elder.getBedId() != null) {
            throw new com.example.eldercare.common.BusinessException("该老人已分配床位");
        }

        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new EntityNotFoundException("床位不存在"));

        if (bed.getStatus() != 0) {
            throw new com.example.eldercare.common.BusinessException("床位当前不可用");
        }

        elder.setBedId(bedId);
        elderRepository.save(elder);

        bed.setStatus(1);
        bed.setElderId(elderId);
        bedRepository.save(bed);

        return elder;
    }

    @Transactional
    public Elder releaseBed(Long elderId) {
        Elder elder = elderRepository.findById(elderId)
                .orElseThrow(() -> new EntityNotFoundException("老人档案不存在"));

        Long bedId = elder.getBedId();
        if (bedId == null) {
            throw new com.example.eldercare.common.BusinessException("该老人未分配床位");
        }

        Bed bed = bedRepository.findById(bedId)
                .orElseThrow(() -> new EntityNotFoundException("床位不存在"));

        elder.setBedId(null);
        elderRepository.save(elder);

        bed.setStatus(0);
        bed.setElderId(null);
        bedRepository.save(bed);

        return elder;
    }

    public Map<String, Object> getElderStats() {
        Map<String, Object> stats = new HashMap<>();
        List<Elder> elders = elderRepository.findByStatus(1);

        stats.put("total", elders.size());
        stats.put("nursingLevel1", elders.stream().filter(e -> "一级".equals(e.getNursingLevel())).count());
        stats.put("nursingLevel2", elders.stream().filter(e -> "二级".equals(e.getNursingLevel())).count());
        stats.put("nursingLevel3", elders.stream().filter(e -> "三级".equals(e.getNursingLevel())).count());

        return stats;
    }
}
