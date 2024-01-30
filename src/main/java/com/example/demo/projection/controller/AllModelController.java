package com.example.demo.projection.controller;

import com.example.demo.projection.model.Department;
import com.example.demo.projection.model.Employee;
import com.example.demo.projection.service.AllModelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projection")
public class AllModelController {

    private final AllModelService service;

    public AllModelController(AllModelService service) {
        this.service = service;
    }

    @GetMapping("/full-name/{id}")
    public String getFullName(@PathVariable("id") long id) {
        return service.findByIdDto(id).getFullName();
    }

    @GetMapping("/position/{id}")
    public String position(@PathVariable("id") long id) {
        return service.findByIdDto(id).getPosition();
    }

    @GetMapping("/department-name/{id}")
    public String departmentName(@PathVariable("id") long id) {
        return service.findByIdDto(id).getDepartmentName();
    }

    @GetMapping("/{id}")
    public Employee findByid(@PathVariable("id") long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<Employee> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Employee post(@RequestBody Employee employee) {
        return service.save(employee);
    }

    @PatchMapping("/{id}")
    public Employee patch(@PathVariable("id") long id, @RequestBody Employee employee) {
        return service.update(id, employee);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        service.delete(id);
    }

    @GetMapping("/department/{id}")
    public Department findOne(@PathVariable("id") long id) {
        return service.findOneDepartment(id);
    }

    @GetMapping("/department")
    public List<Department> findAllDepartment() {
        return service.findAllDepartment();
    }

    @PostMapping("/department")
    public Department postDep(@RequestBody Department department) {
        return service.saveDepartment(department);
    }

    @PatchMapping("/department/{id}")
    public Department updateDepartment(@PathVariable("id") long id, @RequestBody Department department) {
        return service.updateDepartment(id, department);
    }

    @DeleteMapping("/department/{id}")
    public void deleteDep(@PathVariable("id") long id) {
        service.deleteDep(id);
    }


}
