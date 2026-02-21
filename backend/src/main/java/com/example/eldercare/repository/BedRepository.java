package com.example.eldercare.repository;

import com.example.eldercare.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BedRepository extends JpaRepository<Bed, Long>, JpaSpecificationExecutor<Bed> {
    boolean existsByBedNo(String bedNo);

    Bed findByElderId(Long elderId);

    Long countByStatus(Integer status);
}
