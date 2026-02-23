package com.example.eldercare.repository;

import com.example.eldercare.entity.ActivityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityPostRepository extends JpaRepository<ActivityPost, Long> {

    Page<ActivityPost> findByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);

    Page<ActivityPost> findByElderIdAndStatusOrderByCreateTimeDesc(Long elderId, Integer status, Pageable pageable);

    Page<ActivityPost> findByActivityTypeAndStatusOrderByCreateTimeDesc(String activityType, Integer status, Pageable pageable);

    @Query("SELECT p FROM ActivityPost p WHERE p.status = 1 AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) ORDER BY p.createTime DESC")
    Page<ActivityPost> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM ActivityPost p WHERE p.status = 1 AND p.createTime >= :startTime ORDER BY p.likeCount DESC, p.createTime DESC")
    List<ActivityPost> findHotPosts(@Param("startTime") LocalDateTime startTime);

    @Query("SELECT p FROM ActivityPost p WHERE p.isPinned = true AND p.status = 1 ORDER BY p.createTime DESC")
    List<ActivityPost> findPinnedPosts();

    @Query("SELECT p FROM ActivityPost p WHERE p.activityDate >= :startDate AND p.activityDate <= :endDate ORDER BY p.activityDate DESC")
    Page<ActivityPost> findByActivityDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("SELECT COUNT(p) FROM ActivityPost p WHERE p.elderId = :elderId AND p.status = 1")
    Long countByElderId(@Param("elderId") Long elderId);

    @Query("SELECT COUNT(p) FROM ActivityPost p WHERE p.elderId = :elderId AND p.status = 1 AND p.createTime >= :startTime")
    Long countByElderIdAndCreateTimeAfter(@Param("elderId") Long elderId, @Param("startTime") LocalDateTime startTime);
}
