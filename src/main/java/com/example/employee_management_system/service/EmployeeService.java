package com.example.employee_management_system.service;

import com.example.employee_management_system.model.Employee;
import java.util.List;
import org.springframework.stereotype.Service;

public interface EmployeeService {

    List<Employee> findAllEmployees();

}
