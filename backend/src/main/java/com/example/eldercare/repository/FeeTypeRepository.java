package com.example.eldercare.repository;

import com.example.eldercare.entity.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeTypeRepository extends JpaRepository<FeeType, Long>, JpaSpecificationExecutor<FeeType> {
    boolean existsByFeeType(String feeType);
}
