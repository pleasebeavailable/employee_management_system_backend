package com.example.employee_management_system.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService service;

    @Test
    public void getEmployees() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee(1, "ante", "ante", "ante@ante.com"));
        employees.add(new Employee(2, "ante2", "ante2", "ante2@ante.com"));
        Mockito.when(service.findAllEmployees()).thenReturn(employees);
        mockMvc.perform(get("/employee/getAll")).andExpect(status().isOk()).andExpect(content().string(
                CoreMatchers.containsString("1")));
    }

    @ParameterizedTest
    @MethodSource("getEmployeeId")
    public void getEmployee(Long id) throws Exception {

        Employee employee = new Employee(1, "ante", "ante", "ante@ante.com");
        Mockito.when(service.findEmployee(id)).thenReturn(employee);
        mockMvc.perform(get("/employee/get/1")).andExpect(status().isOk()).andExpect(content().string(
                CoreMatchers.containsString("1")));
    }

    @ParameterizedTest
    @MethodSource("getEmployee")
    public void addEmployee(Employee employee) throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer();

        Employee mockEmployee = new Employee(1L, "ante", "ante", "ante@ante.com");
        Mockito.when(service.saveEmployee(employee)).thenReturn(mockEmployee);
        mockMvc.perform(post("/employee/add")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectWriter.writeValueAsString(employee))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        CoreMatchers.containsString("1")));
    }

    @ParameterizedTest
    @MethodSource("getEmployee")
    public void editEmployee(Employee employee) throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer();

        Employee mockEmployee = new Employee(1L, "ante2", "ante", "ante@ante.com");
        Mockito.when(service.saveEmployee(employee)).thenReturn(mockEmployee);
        mockMvc.perform(put("/employee/edit")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectWriter.writeValueAsString(employee))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        CoreMatchers.containsString("ante2")));
    }

    @ParameterizedTest
    @MethodSource("getEmployeeId")
    public void deleteEmployee(Long id) throws Exception {
        ObjectWriter objectWriter = new ObjectMapper().writer();

        mockMvc.perform(delete("/employee/delete/" + id))
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> getEmployeeId() {
        return Stream.of(
                Arguments.of(1L)
        );
    }

    private static Stream<Arguments> getEmployee() {
        return Stream.of(
                Arguments.of(new Employee("ante", "ante", "ante@ante.com"))
        );
    }
}

