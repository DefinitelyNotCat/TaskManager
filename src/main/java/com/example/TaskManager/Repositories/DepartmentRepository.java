package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department getDepartmentById(Integer id);

    /*@Query(value = "SELECT * FROM EMPLOYEE WHERE EME_DEPARTMENT = ?1", nativeQuery = true)
    List<Employee> getEmployeesByDepartment(Integer departmentId);*/
}
