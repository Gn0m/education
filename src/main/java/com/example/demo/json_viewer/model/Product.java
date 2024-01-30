package com.example.demo.json_viewer.model;

import com.example.demo.json_viewer.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.UserDetails.class,Views.OrderDetails.class})
    private long id;
    @JsonView({Views.UserDetails.class,Views.OrderDetails.class})
    private String name;
    @JsonView({Views.UserDetails.class,Views.OrderDetails.class})
    private BigDecimal price;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    public Product() {
    }

    public Product(String name, BigDecimal price,Order order) {
        this.name = name;
        this.price = price;
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", order=" + order +
                '}';
    }
}
