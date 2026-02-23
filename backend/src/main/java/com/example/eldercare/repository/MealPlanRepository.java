package com.example.eldercare.repository;

import com.example.eldercare.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findByMealDate(LocalDate mealDate);

    List<MealPlan> findByMealDateBetween(LocalDate startDate, LocalDate endDate);

    List<MealPlan> findByMealDateAndMealType(LocalDate mealDate, String mealType);

    @Query("SELECT m FROM MealPlan m WHERE m.mealDate >= :startDate AND m.mealDate <= :endDate ORDER BY m.mealDate, m.mealType")
    List<MealPlan> findBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<MealPlan> findByMealDateOrderByMealType(LocalDate mealDate);

    @Query("SELECT m FROM MealPlan m WHERE m.mealDate >= :date ORDER BY m.mealDate, m.mealType")
    List<MealPlan> findFromToday(@Param("date") LocalDate date);
}
