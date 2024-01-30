package com.example.demo.projection.repo;

import com.example.demo.projection.model.Employee;
import com.example.demo.projection.model.dto.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    EmployeeDTO findEmployeeById(long id);

    @Query("SELECT new com.example.demo.projection.model.dto.EmployeeDTO(e.firstName,e.lastName,e.position,e.department) from Employee e")
    List<EmployeeDTO> findAllEmployee();
}
