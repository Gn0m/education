package com.example.demo.projection.model.dto;

import com.example.demo.projection.model.Department;
import com.example.demo.projection.repo.EmployeeProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO implements EmployeeProjection {

    private String firstName;
    private String lastName;
    private String position;
    private Department department;

    public EmployeeDTO(String firstName, String lastName, String position, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
    }

    @Override
    public String getFullName() {
        return firstName.concat(" ").concat(lastName);
    }

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public String getDepartmentName() {
        return department.getName();
    }
}
