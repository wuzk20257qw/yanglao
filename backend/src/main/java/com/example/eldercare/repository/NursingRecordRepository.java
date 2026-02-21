package com.example.eldercare.repository;

import com.example.eldercare.entity.NursingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NursingRecordRepository extends JpaRepository<NursingRecord, Long>, JpaSpecificationExecutor<NursingRecord> {

    Page<NursingRecord> findByElderIdOrderByCreateTimeDesc(Long elderId, Pageable pageable);
}
