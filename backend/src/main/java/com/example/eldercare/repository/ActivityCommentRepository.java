package com.example.eldercare.repository;

import com.example.eldercare.entity.ActivityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityCommentRepository extends JpaRepository<ActivityComment, Long> {

    List<ActivityComment> findByPostIdAndParentIdIsNullOrderByCreateTimeAsc(Long postId);

    Page<ActivityComment> findByPostIdOrderByCreateTimeAsc(Long postId, Pageable pageable);

    List<ActivityComment> findByPostIdOrderByCreateTimeAsc(Long postId);

    List<ActivityComment> findByParentIdOrderByCreateTimeAsc(Long parentId);

    List<ActivityComment> findByUserIdOrderByCreateTimeDesc(Long userId);

    Page<ActivityComment> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM ActivityComment c WHERE c.postId = :postId")
    Long countByPostId(@Param("postId") Long postId);
}
