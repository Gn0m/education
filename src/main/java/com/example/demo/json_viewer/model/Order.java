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
@Table(name = "ordr")
public class Order {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.UserDetails.class, Views.OrderDetails.class})
    private long id;
    @Setter
    @JsonView({Views.UserDetails.class, Views.OrderDetails.class})
    private BigDecimal sum;
    @JsonView({Views.UserDetails.class, Views.OrderDetails.class})
    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Product> products;
    @Setter
    @JsonView({Views.UserDetails.class, Views.OrderDetails.class})
    private Status status;

    public Order() {
    }

    public Order(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        sum = new BigDecimal(
                this.products.stream().map(Product::getPrice).mapToInt(BigDecimal::intValue).sum());
    }

    public Status getStatus() {
        return status;
    }


}
