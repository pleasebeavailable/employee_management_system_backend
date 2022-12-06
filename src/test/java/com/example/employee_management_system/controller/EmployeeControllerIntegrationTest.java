package com.example.employee_management_system.controller;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.employee_management_system.model.Employee;
import com.example.employee_management_system.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void getEmployees() throws Exception {
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(new Employee(1, "test", "test", "test@test.com"));
        employeeList.add(new Employee(2, "test2", "test2", "test2@test.com"));

        ObjectWriter objectWriter = new ObjectMapper().writer();
        for (Employee e : employeeList) {
            mockMvc.perform(post("/employee/add")
                            .characterEncoding("utf-8")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectWriter.writeValueAsString(e))
                    )
                    .andExpect(status().isOk());
        }
        MvcResult result = mockMvc.perform(get("/employee/getAll")).andExpect(status().isOk()).andReturn();
        String resEmployeesString = result.getResponse().getContentAsString();
        Employee[] employees = new ObjectMapper().readValue(resEmployeesString, Employee[].class);
        assertThat(employees).isNotEmpty();
    }

    @Test
    public void getEmployee() throws Exception {
        Employee employee = new Employee("testGet", "testGet", "testGet@testGet.com");
        ObjectWriter objectWriter = new ObjectMapper().writer();

        MvcResult result = mockMvc.perform(post("/employee/add")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(employee))
                )
                .andExpect(status().isOk()).andReturn();

        String resEmployeeString = result.getResponse().getContentAsString();
        Employee resEmployee = new ObjectMapper().readValue(resEmployeeString, Employee.class);

        mockMvc.perform(get("/employee/get/" + resEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        CoreMatchers.containsString("testGet")));
    }

    @Test
    public void createEmployee() throws Exception {
        Employee employee = new Employee("novi", "novi", "novi@novi.com");
        ObjectWriter objectWriter = new ObjectMapper().writer();

        mockMvc.perform(post("/employee/add")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(employee))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        CoreMatchers.containsString("novi"))).andDo(print());
    }


    @Test
    public void editEmployee() throws Exception {
        Employee employee = new Employee("edit", "edit", "edit@edit.com");
        ObjectWriter objectWriter = new ObjectMapper().writer();

        MvcResult result = mockMvc.perform(post("/employee/add")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(employee))
                )
                .andExpect(status().isOk()).andReturn();

        String resEmployeeString = result.getResponse().getContentAsString();
        Employee resEmployee = new ObjectMapper().readValue(resEmployeeString, Employee.class);

        Employee insertedEmployee = employeeService.findEmployee(resEmployee.getId());
        Employee updatedEmployee = new Employee(insertedEmployee.getId(), "editSuc", "editSuc", "editSuc@editSuc.com");

        mockMvc.perform(put("/employee/edit")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(updatedEmployee))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers
                        .containsString("editSuc")));
    }

    @Test
    public void deleteEmployee() throws Exception {
        Employee employee = new Employee("delete", "delete", "delete@delete.com");
        ObjectWriter objectWriter = new ObjectMapper().writer();

        MvcResult result = mockMvc.perform(post("/employee/add")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(employee))
                )
                .andExpect(status().isOk()).andReturn();

        String resEmployeeString = result.getResponse().getContentAsString();
        Employee resEmployee = new ObjectMapper().readValue(resEmployeeString, Employee.class);

        Employee employeeToDelete = employeeService.findEmployee(resEmployee.getId());

        mockMvc.perform(delete("/employee/delete/" + employeeToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                )
                .andExpect(status().isOk());
    }
}
