package com.example.demo.json_viewer.controller;

import com.example.demo.json_viewer.model.Order;
import com.example.demo.json_viewer.model.User;
import com.example.demo.json_viewer.service.UserService;
import com.example.demo.json_viewer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/summary/{id}")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<User> getOneSummary(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<User> getOneDetails(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getOne(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/all-paging")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<List<User>> getAllPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return new ResponseEntity<>(service.getAllPaging(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @PostMapping()
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<User> post(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.put(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        service.delete(id);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<User> update(@Valid @RequestBody User user, @PathVariable long id) {
        return new ResponseEntity<>(service.update(id, user), HttpStatus.OK);
    }

    @GetMapping("/init")
    public ResponseEntity<Boolean> init() {
        service.init();
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @GetMapping("/orders")
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(service.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> findOrderById(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getOrderById(id), HttpStatus.OK);
    }

    @PostMapping("/orders")
    @Transactional
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> post(@RequestBody Order order) {
        return new ResponseEntity<>(service.putOrder(order), HttpStatus.OK);
    }

    @PatchMapping("/orders/{id}")
    @Transactional
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> patchOrder(@PathVariable("id") long id,@RequestBody Order order) {
        return new ResponseEntity<>(service.updateOrder(id, order), HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.deleteOrder(id), HttpStatus.OK);
    }
}
