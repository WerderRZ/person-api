package com.werdersoft.personapi.employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(UUID id);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

}
