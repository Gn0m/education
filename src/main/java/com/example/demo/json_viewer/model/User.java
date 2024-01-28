package com.example.demo.json_viewer.model;

import com.example.demo.json_viewer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Views.UserSummary.class, Views.UserDetails.class})
    private long id;
    @JsonView({Views.UserSummary.class, Views.UserDetails.class})
    @NotNull(message = "name not null")
    private String name;
    @JsonView({Views.UserSummary.class, Views.UserDetails.class})
    @Email(message = "email not valid")
    @NotNull(message = "email not null")
    private String email;
    @JsonView({Views.UserDetails.class, Views.UserDetails.class})
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.orders = new ArrayList<>();
    }

    public User() {
        this.orders = new ArrayList<>();
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
