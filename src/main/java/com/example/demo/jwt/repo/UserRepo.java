package com.example.demo.jwt.repo;

import com.example.demo.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "jwtUserRepo")
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);


}
