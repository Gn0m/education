package com.example.demo.jwt.repo;

import com.example.demo.jwt.model.UserAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserAttemptsRepo extends JpaRepository<UserAttempts, Long> {

    Optional<UserAttempts> findByLogin(String login);
}
