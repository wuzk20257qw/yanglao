package com.example.eldercare.repository;

import com.example.eldercare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByRole(String role);

    List<User> findByElderId(Long elderId);
}
