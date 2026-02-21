package com.example.eldercare.repository;

import com.example.eldercare.entity.ShiftSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ShiftScheduleRepository extends JpaRepository<ShiftSchedule, Long>, JpaSpecificationExecutor<ShiftSchedule> {

    Page<ShiftSchedule> findByNurseId(Long nurseId, Pageable pageable);

    Page<ShiftSchedule> findByNurseIdAndShiftDateBetween(Long nurseId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<ShiftSchedule> findByShiftDateAndStatus(LocalDate shiftDate, Integer status, Pageable pageable);
}
