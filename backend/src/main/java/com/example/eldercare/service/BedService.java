package com.example.eldercare.service;

import com.example.eldercare.dto.BedDTO;
import com.example.eldercare.entity.Bed;
import com.example.eldercare.repository.BedRepository;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BedService {

    private final BedRepository bedRepository;

    @Transactional
    public Bed createBed(BedDTO dto) {
        if (bedRepository.existsByBedNo(dto.getBedNo())) {
            throw new com.example.eldercare.common.BusinessException("床位号已存在");
        }

        Bed bed = new Bed();
        bed.setBedNo(dto.getBedNo());
        bed.setRoomNo(dto.getRoomNo());
        bed.setBuilding(dto.getBuilding());
        bed.setFloor(dto.getFloor());
        bed.setType(dto.getType());
        bed.setPrice(dto.getPrice());
        bed.setStatus(0);

        return bedRepository.save(bed);
    }

    @Transactional
    public Bed updateBed(Long id, BedDTO dto) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("床位不存在"));

        if (!dto.getBedNo().equals(bed.getBedNo()) && bedRepository.existsByBedNo(dto.getBedNo())) {
            throw new com.example.eldercare.common.BusinessException("床位号已存在");
        }

        bed.setBedNo(dto.getBedNo());
        bed.setRoomNo(dto.getRoomNo());
        bed.setBuilding(dto.getBuilding());
        bed.setFloor(dto.getFloor());
        bed.setType(dto.getType());
        bed.setPrice(dto.getPrice());

        return bedRepository.save(bed);
    }

    @Transactional
    public void deleteBed(Long id) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("床位不存在"));

        if (bed.getElderId() != null) {
            throw new com.example.eldercare.common.BusinessException("该床位已被占用，无法删除");
        }

        bedRepository.delete(bed);
    }

    public Bed getBedById(Long id) {
        return bedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("床位不存在"));
    }

    public Page<Bed> getBeds(Integer page, Integer size, String building, Integer floor, String type, Integer status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "building", "floor", "roomNo", "bedNo"));

        Specification<Bed> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (building != null && !building.isEmpty()) {
                predicates.add(cb.like(root.get("building"), "%" + building + "%"));
            }
            if (floor != null) {
                predicates.add(cb.equal(root.get("floor"), floor));
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return bedRepository.findAll(spec, pageable);
    }

    @Transactional
    public Bed updateBedStatus(Long id, Integer status) {
        Bed bed = bedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("床位不存在"));

        bed.setStatus(status);
        return bedRepository.save(bed);
    }
}
