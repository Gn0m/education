package com.example.demo.transaction.controller;

import com.example.demo.transaction.model.Customer;
import com.example.demo.transaction.model.Order;
import com.example.demo.transaction.model.Product;
import com.example.demo.transaction.model.dto.OrderDTO;
import com.example.demo.transaction.service.ServiceProductOrder;
import com.example.demo.transaction.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class OrderController {

    private final ServiceProductOrder service;

    public OrderController(ServiceProductOrder service) {
        this.service = service;
    }

    @GetMapping("/users/{id}/balance")
    @JsonView(Views.Balance.class)
    public Customer getBalance(@PathVariable("id") long id) {
        return service.getCustomer(id);
    }

    @GetMapping("/{id}")
    public Order getOne(@PathVariable("id") long id) {
        return service.getOne(id);
    }

    @GetMapping("/all")
    public List<Order> all() {
        return service.getAll();
    }

    @PostMapping
    public String addOrder(@RequestParam("customerId") long id, @RequestBody OrderDTO dto) {
        return service.placeOrder(id, dto);
    }

    @PostMapping("/customer")
    public Customer create(@RequestBody Customer customer) {
        return service.createCustomer(customer);
    }

    @PostMapping("/product")
    public Product createProduct(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PatchMapping("/{id}")
    public Order patch(@PathVariable("id") long id, @RequestBody OrderDTO order) {
        return service.update(id, order);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        service.deleteOrder(id);
    }
}
