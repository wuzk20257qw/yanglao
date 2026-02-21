package com.example.eldercare.repository;

import com.example.eldercare.entity.ActivityEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityEnrollmentRepository extends JpaRepository<ActivityEnrollment, Long>, JpaSpecificationExecutor<ActivityEnrollment> {

    Page<ActivityEnrollment> findByActivityId(Long activityId, Pageable pageable);

    Page<ActivityEnrollment> findByElderId(Long elderId, Pageable pageable);

    Integer countByActivityIdAndStatus(Long activityId, Integer status);
}
