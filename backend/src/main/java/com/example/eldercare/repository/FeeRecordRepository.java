package com.example.eldercare.repository;

import com.example.eldercare.entity.FeeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long>, JpaSpecificationExecutor<FeeRecord> {

    Page<FeeRecord> findByElderIdOrderByFeeDateDesc(Long elderId, Pageable pageable);

    List<FeeRecord> findByElderId(Long elderId);
}
