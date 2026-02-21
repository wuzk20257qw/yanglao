package com.example.eldercare.repository;

import com.example.eldercare.entity.DiningRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiningRecordRepository extends JpaRepository<DiningRecord, Long>, JpaSpecificationExecutor<DiningRecord> {

    Page<DiningRecord> findByElderId(Long elderId, Pageable pageable);

    Page<DiningRecord> findByElderIdAndDiningDate(Long elderId, LocalDate diningDate, Pageable pageable);

    List<DiningRecord> findByDiningDateBetween(LocalDate startDate, LocalDate endDate);

    List<DiningRecord> findByElderIdAndDiningDateBetween(Long elderId, LocalDate startDate, LocalDate endDate);
}
