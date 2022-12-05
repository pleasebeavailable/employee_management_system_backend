package com.example.employee_management_system.service;

import com.example.employee_management_system.exception.ResourceNotFoundException;
import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found!"));
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }


    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
