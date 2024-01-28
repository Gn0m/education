package com.example.demo.json_viewer.controller;

import com.example.demo.json_viewer.model.User;
import com.example.demo.json_viewer.service.UserService;
import com.example.demo.json_viewer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @JsonView(Views.UserSummary.class)
    @GetMapping("/summary/{id}")
    public ResponseEntity<User> getOneSummary(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }

    @JsonView(Views.UserDetails.class)
    @GetMapping("/details/{id}")
    public ResponseEntity<User> getOneDetails(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }

    @JsonView(Views.UserSummary.class)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @JsonView(Views.UserSummary.class)
    @PostMapping()
    public ResponseEntity<User> post(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.put(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        service.delete(id);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @JsonView(Views.UserSummary.class)
    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@Valid @RequestBody User user, @PathVariable long id) {
        return new ResponseEntity<>(service.update(id, user), HttpStatus.OK);
    }

    @GetMapping("/init")
    public ResponseEntity<Boolean> init() {
        service.init();
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}
