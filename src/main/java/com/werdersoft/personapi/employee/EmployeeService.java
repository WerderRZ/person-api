package com.werdersoft.personapi.employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTOResponse> getAllEmployees();

    Employee getEmployeeById(Long id);

    Employee createEmployee(EmployeeDTORequest employee);

    Employee updateEmployeeById(Long id, Employee employee);

    void deleteEmployeeById(Long id);
}
