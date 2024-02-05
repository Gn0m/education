package com.example.demo.jwt.controller;

import com.example.demo.jwt.model.*;
import com.example.demo.jwt.util.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
@Slf4j
public class HelloController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<User> register(@PathVariable("id") Long id, @RequestBody BlockRequest request) {
        return ResponseEntity.ok(service.block(id, request));
    }

    @GetMapping
    public String hello() {
        return "Hello";
    }

    @GetMapping("/user")
    public String user() {
        return "User";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin";
    }

    @PostMapping("/role/{id}")
    public ResponseEntity<User> change(@PathVariable("id") int id, @RequestBody UserDtoRole user) {
        return new ResponseEntity<>(service.updateRole(id, user), HttpStatus.OK);
    }
}
