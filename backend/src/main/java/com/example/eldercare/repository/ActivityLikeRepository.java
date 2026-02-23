package com.example.eldercare.repository;

import com.example.eldercare.entity.ActivityLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityLikeRepository extends JpaRepository<ActivityLike, Long> {

    Optional<ActivityLike> findByPostIdAndUserId(Long postId, Long userId);

    void deleteByPostIdAndUserId(Long postId, Long userId);

    @Query("SELECT COUNT(l) FROM ActivityLike l WHERE l.postId = :postId")
    Long countByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(l) FROM ActivityLike l WHERE l.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);

    Page<ActivityLike> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);
}
