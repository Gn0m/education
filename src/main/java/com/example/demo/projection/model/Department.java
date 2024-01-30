package com.example.demo.projection.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Department {

    public Department() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "department_generator")
    @SequenceGenerator(name = "department_generator", sequenceName = "department_seq", allocationSize = 1)
    private long id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private Set<Employee> employeeSet;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department department = (Department) o;
        return id == department.id && Objects.equals(name, department.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
