package com.example.demo.projection.service;

import com.example.demo.projection.model.Department;
import com.example.demo.projection.model.Employee;
import com.example.demo.projection.model.dto.EmployeeDTO;
import com.example.demo.projection.repo.DepartmentRepo;
import com.example.demo.projection.repo.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.demo.json_viewer.exceptions.NotFoundUserException.notFoundUser;

@Service
public class AllModelService {

    private DepartmentRepo departmentRepo;
    private EmployeeRepo employeeRepo;

    public AllModelService(DepartmentRepo departmentRepo, EmployeeRepo employeeRepo) {
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    public EmployeeDTO findByIdDto(long id) {
        return employeeRepo.findEmployeeById(id);
    }

    public Employee findById(long id) {
        return employeeRepo.findById(id).orElseThrow(
                notFoundUser("Сотрудник с {0} не найдён", id)
        );
    }

    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public Employee save(Employee employee) {
        Department department = departmentRepo.findById(employee.getDepartment().getId())
                .orElseThrow(notFoundUser("Департамент с {0} не найден", employee.getDepartment().getId()));
        employee.setDepartment(department);
        return employeeRepo.save(employee);
    }

    public Employee update(long id, Employee employee) {
        Optional<Employee> optional = employeeRepo.findById(id);
        Employee save = optional.orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id));
        Department department = departmentRepo.findById(save.getDepartment().getId())
                .orElseThrow(notFoundUser("Департамент с {0} не найден", id));

        save.setFirstName(employee.getFirstName());
        save.setLastName(employee.getLastName());
        save.setPosition(employee.getPosition());
        save.setSalary(employee.getSalary());
        save.setDepartment(department);

        return employeeRepo.save(save);
    }

    public void delete(long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(
                notFoundUser("Пользователь с {0} не найден", id));
        employeeRepo.delete(employee);
    }

    public Department findOneDepartment(long id) {
        return departmentRepo.findById(id).orElseThrow(
                notFoundUser("Департамент с {0} не найден", id)
        );
    }

    public List<Department> findAllDepartment() {
        return departmentRepo.findAll();
    }

    public Department saveDepartment(Department department) {
        return departmentRepo.save(department);
    }

    public Department updateDepartment(long id, Department department) {
        Department dep = departmentRepo.findById(id).orElseThrow(
                notFoundUser("Департамент с {0} не найден", id));

        dep.setName(department.getName());
        return departmentRepo.save(dep);
    }

    public void deleteDep(long id) {
        Department department = departmentRepo.findById(id).orElseThrow(
                notFoundUser("Департамент с {0} не найден", id));
        departmentRepo.delete(department);
    }
}
