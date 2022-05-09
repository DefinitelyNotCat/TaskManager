package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Employee;
import com.example.TaskManager.DataModels.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findEmployeeByLogin(String login);
    Employee findEmployeeById(Long id);
    Employee findEmployeeByEmail(String email);
    List<Employee> findEmployeesByDepartment(Department department);
}
