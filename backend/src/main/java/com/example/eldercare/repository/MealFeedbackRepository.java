package com.example.eldercare.repository;

import com.example.eldercare.entity.MealFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealFeedbackRepository extends JpaRepository<MealFeedback, Long> {

    List<MealFeedback> findByMealPlanId(Long mealPlanId);

    Page<MealFeedback> findByElderId(Long elderId, Pageable pageable);

    Page<MealFeedback> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT AVG(m.rating) FROM MealFeedback m WHERE m.mealPlanId = :mealPlanId")
    Double getAverageRating(@Param("mealPlanId") Long mealPlanId);

    @Query("SELECT COUNT(m) FROM MealFeedback m WHERE m.mealPlanId = :mealPlanId")
    Long getFeedbackCount(@Param("mealPlanId") Long mealPlanId);

    @Query("SELECT m FROM MealFeedback m WHERE m.createTime >= :startTime ORDER BY m.createTime DESC")
    List<MealFeedback> findRecentFeedback(@Param("startTime") LocalDateTime startTime);

    List<MealFeedback> findByElderIdOrderByCreateTimeDesc(Long elderId);
}
