package com.example.demo.jwt.util;

import com.example.demo.json_viewer.exceptions.NotFoundUserException;
import com.example.demo.json_viewer.exceptions.UserAlredyExistsException;
import com.example.demo.jwt.model.*;
import com.example.demo.jwt.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.json_viewer.exceptions.NotFoundUserException.notFoundUser;

@Service
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager manager;
    @Qualifier(value = "jwtUserRepo")
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserAttemptsService attemptsService;

    public AuthenticationService(AuthenticationManager manager, UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService, UserAttemptsService attemptsService) {
        this.manager = manager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.attemptsService = attemptsService;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<User> byLogin = userRepo.findByLogin(request.getLogin());
        if (byLogin.isPresent()) {
            throw new UserAlredyExistsException("User alredy exists");
        }

        User user = User.builder()
                .login(request.getLogin())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountNonLocked(true)
                .roles(request.getRoles())
                .build();

        attemptsService.add(request.getLogin());

        User save = userRepo.save(user);

        Map<String, String> claims = getClaims(save);

        var jwtToken = jwtService.generateToken(claims, save);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private static Map<String, String> getClaims(User save) {
        Map<String, String> claims = new HashMap<>();
        claims.put("firstname", save.getFirstname());
        claims.put("lastname", save.getLastname());
        claims.put("email", save.getEmail());
        claims.put("roles", save.getRoles().toString());
        return claims;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(), request.getPassword()
        ));
        User user = userRepo.findByLogin(request.getLogin())
                .orElseThrow(() -> new NotFoundUserException("Not found"));

        Map<String, String> claims = getClaims(user);

        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public User updateRole(long id, UserDtoRole user) {
        User bdUser = userRepo.findById(id).orElseThrow(notFoundUser("Пользователь с {0} не найден.", id));
        User save = userRepo
                .save(updateRole(bdUser, user));
        log.info("User Id: " + save.getId() + " change role to: " + user.getRoles());
        return save;
    }

    private User updateRole(User from, UserDtoRole where) {
        from.getRoles().clear();
        from.getRoles().addAll(where.getRoles());
        return from;
    }

    public User block(long id, BlockRequest request) {
        User user = userRepo.findById(id).orElseThrow(
                notFoundUser("Пользователь с {0} не найден.", id));
        user.setAccountNonLocked(request.getBlock());
        return userRepo.save(user);
    }
}
