package com.example.demo.json_viewer.model;

import com.example.demo.json_viewer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.UserSummary.class, Views.UserDetails.class, Views.OrderDetails.class})
    private long id;
    @JsonView({Views.UserSummary.class, Views.UserDetails.class, Views.OrderDetails.class})
    @NotNull(message = "name not null")
    private String name;
    @JsonView({Views.UserSummary.class, Views.UserDetails.class, Views.OrderDetails.class})
    @Email(message = "email not valid")
    @NotNull(message = "email not null")
    private String email;
    @JsonView(Views.UserDetails.class)
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<Order> orders;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.orders = new HashSet<>();
    }

    public User() {
        this.orders = new HashSet<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
