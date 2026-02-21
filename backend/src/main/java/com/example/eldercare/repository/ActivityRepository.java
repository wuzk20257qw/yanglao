package com.example.eldercare.repository;

import com.example.eldercare.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {

    Page<Activity> findByActivityDate(LocalDate activityDate, Pageable pageable);

    Page<Activity> findByStatus(Integer status, Pageable pageable);
}
