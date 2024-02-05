package com.example.demo.jwt.util;

import com.example.demo.jwt.model.User;
import com.example.demo.jwt.model.UserAttempts;
import com.example.demo.jwt.repo.UserAttemptsRepo;
import com.example.demo.jwt.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.demo.json_viewer.exceptions.NotFoundUserException.notFoundUser;

@Service
public class UserAttemptsService {

    private final UserAttemptsRepo repo;
    private final UserRepo userRepo;
    private static final int MAX_ATTEMPT = 3;

    public UserAttemptsService(UserAttemptsRepo repo, UserRepo userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public void resetFailAttempts(String login) {
        UserAttempts userAttempt = repo.findByLogin(login).orElseThrow(
                notFoundUser("Пользователь " + login + " не найден")
        );
        userAttempt.setCount(MAX_ATTEMPT);
        userAttempt.setLastModifier(new Date());
        repo.save(userAttempt);
    }

    public void updateFailAttempts(String login) {
        UserAttempts userAttempt = repo.findByLogin(login).orElseThrow(
                notFoundUser("Пользователь " + login + " не найден")
        );

        if (userAttempt.getCount() - 1 == 0) {
            User user = userRepo.findByLogin(login).orElseThrow(
                    notFoundUser("Пользователь " + login + " не найден")
            );

            userAttempt.setCount(MAX_ATTEMPT);
            user.setAccountNonLocked(false);

            userRepo.save(user);
        } else {
            userAttempt.setCount(userAttempt.getCount() - 1);
        }
        userAttempt.setLastModifier(new Date());
        repo.save(userAttempt);
    }

    public UserAttempts getUserAttempts(String login) {
        return repo.findByLogin(login).orElseThrow(
                notFoundUser("Пользователь " + login + " не найден"));
    }

    public void add(String login) {
        UserAttempts userAttempts = UserAttempts.builder()
                .count(MAX_ATTEMPT)
                .lastModifier(new Date())
                .login(login).build();
        repo.save(userAttempts);
    }
}
