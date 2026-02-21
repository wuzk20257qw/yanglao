package com.example.eldercare.repository;

import com.example.eldercare.entity.HealthRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long>, JpaSpecificationExecutor<HealthRecord> {

    Page<HealthRecord> findByElderIdOrderByRecordTimeDesc(Long elderId, Pageable pageable);

    @Query("SELECT h FROM HealthRecord h WHERE h.elderId = :elderId ORDER BY h.recordTime DESC")
    Page<HealthRecord> findLatestByElderId(@Param("elderId") Long elderId, Pageable pageable);

    List<HealthRecord> findByElderIdAndRecordTimeBetween(Long elderId, LocalDateTime startTime, LocalDateTime endTime);

    List<HealthRecord> findByElderIdAndRecordTimeBetweenOrderByRecordTime(Long elderId, LocalDateTime startTime, LocalDateTime endTime);
}
