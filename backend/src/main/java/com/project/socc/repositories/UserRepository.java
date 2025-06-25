package com.project.socc.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.socc.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Page<User> findByUserNameContainingIgnoreCase(String username, Pageable pageable);
}
