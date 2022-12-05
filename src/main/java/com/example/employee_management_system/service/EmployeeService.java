package com.example.employee_management_system.service;

import com.example.employee_management_system.model.Employee;
import java.util.List;

public interface EmployeeService {

    List<Employee> findAllEmployees();

    Employee findEmployee(Long id);

    Employee saveEmployee(Employee employee);

    void deleteEmployee(Long id);
}
