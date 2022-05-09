package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Department;
import com.example.TaskManager.Repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getDepartment() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Integer id) {
        return departmentRepository.getDepartmentById(id);
    }

    /*public List<Employee> getEmployees(Department department) {
        return departmentRepository.getEmployeesByDepartment(department.getId());
    }*/
}