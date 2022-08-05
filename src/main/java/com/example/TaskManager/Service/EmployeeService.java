package com.example.TaskManager.Service;

import com.example.TaskManager.DataModels.Employee;
import com.example.TaskManager.DataModels.Department;
import com.example.TaskManager.DataModels.Task;
import com.example.TaskManager.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, DepartmentService departmentService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
    }

    //Получить map сотрудников по отделам
    public Map<Integer, Map<Long, String>> mapDepartmentsAndEmployees() {
        Map<Integer, Map<Long, String>> depEmpMap = new HashMap<>();

        for (Department department : departmentService.getDepartment()) {
            List<Employee> employees = new ArrayList<>(getEmployeeByDepartment(department));
            Map<Long, String> strings = new HashMap<>();

            for (int i = 0; i< employees.size(); i++) {
                strings.put(employees.get(i).getId(), employees.get(i).getFullName());
            }
            depEmpMap.put(department.getId(), strings);
        }
        return depEmpMap;
    }

    public List<Employee> getEmployee() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.getById(id);
    }

    public List<Employee> getEmployeeByDepartment(Department department) {
        return employeeRepository.findEmployeesByDepartment(department);
    }

    public Employee getCurrentEmployee() {
        Employee user = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Transactional
    public void saveEmployee(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    @Transactional
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return employeeRepository.findEmployeeByLogin(login);
    }
}
