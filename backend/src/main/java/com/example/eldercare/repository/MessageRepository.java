package com.example.eldercare.repository;

import com.example.eldercare.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByUserIdAndTypeOrderByCreateTimeDesc(Long userId, String type, Pageable pageable);

    Page<Message> findByUserIdAndIsReadOrderByCreateTimeDesc(Long userId, Boolean isRead, Pageable pageable);

    Page<Message> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    @Query("SELECT m.type, COUNT(m) FROM Message m WHERE m.userId = :userId AND m.isRead = false GROUP BY m.type")
    List<Object[]> countUnreadByType(Long userId);

    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
