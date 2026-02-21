package com.example.eldercare.repository;

import com.example.eldercare.entity.AlarmRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRecordRepository extends JpaRepository<AlarmRecord, Long>, JpaSpecificationExecutor<AlarmRecord> {

    Page<AlarmRecord> findByElderIdOrderByCreateTimeDesc(Long elderId, Pageable pageable);

    Page<AlarmRecord> findByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);

    Page<AlarmRecord> findAllByOrderByCreateTimeDesc(Pageable pageable);
}
