package com.example.demo.json_viewer.model;

import com.example.demo.json_viewer.enums.Status;
import com.example.demo.json_viewer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ordr")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.UserDetails.class)
    private long id;
    @JsonView(Views.UserDetails.class)
    private BigDecimal sum;
    @JsonView(Views.UserDetails.class)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Product> products;
    @JsonView(Views.UserDetails.class)
    private Status status;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Order() {
    }

    public Order(List<Product> products, Status status, User user) {
        this.products = products;
        this.status = status;
        this.user = user;
        sum = new BigDecimal(
                products.stream().map(Product::getPrice).mapToInt(BigDecimal::intValue).sum());
    }
}
