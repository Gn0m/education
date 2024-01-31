package com.example.demo.transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "torder")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private double totalAmount;
    @OneToMany
    @JsonIgnore
    private List<Product> products = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ordertr_id")
    private List<ProductInOrder> productInOrders = new ArrayList<>();

    public Order() {
    }

    public void addInOrder(ProductInOrder product) {
        productInOrders.add(product);
    }

    public void addInProduct(ProductInOrder product) {
        this.products.add(new Product(product));
    }

    public void deleteCusAndProd() {
        this.customer = null;
        this.products = null;
    }

    public void deleteProductsAndOrderProd(){
        products.clear();
        productInOrders.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(customer, order.customer) && Objects.equals(totalAmount, order.totalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, totalAmount);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
