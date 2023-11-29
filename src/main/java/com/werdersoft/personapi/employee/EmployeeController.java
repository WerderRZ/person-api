package com.werdersoft.personapi.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDTOResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody EmployeeDTORequest employeeDTORequest) {
        return employeeService.createEmployee(employeeDTORequest);
    }

}
