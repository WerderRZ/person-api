package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.enums.Position;
import com.werdersoft.personapi.person.PersonService;
import com.werdersoft.personapi.subdivision.SubdivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.werdersoft.personapi.util.Utils.toList;
import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PersonService personService;
    private final SubdivisionService subdivisionService;

    @Override
    public List<EmployeeDTOResponse> getAllEmployees() {
        List<EmployeeDTOResponse> employeeDTOResponseList = new ArrayList<>();
        employeeRepository.findAll().forEach(employee -> employeeDTOResponseList.add(employee.toEmployeeResponse()));
        return employeeDTOResponseList;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return findEmployeeById(id);
    }

    @Override
    public Employee createEmployee(EmployeeDTORequest employeeDTORequest) {
        Person person = personService.getPersonById(employeeDTORequest.getPerson_id());
        Subdivision subdivision = subdivisionService.getSubdivisionById(employeeDTORequest.getSubdivision_id());
        return employeeRepository.save(employeeDTORequest.toEmployeeEntity(person, subdivision));
    }

    @Override
    public Employee updateEmployeeById(Long id, Employee employee) {
        Employee findedEmployee = findEmployeeById(id);
        findedEmployee.setPosition(employee.getPosition());
        findedEmployee.setSalary(employee.getSalary());
        findedEmployee.setPerson(employee.getPerson());
        return employeeRepository.save(findedEmployee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        Employee findedEmployee = findEmployeeById(id);
        employeeRepository.delete(findedEmployee);
    }

    private Employee findEmployeeById(Long id) {
        return toValue(employeeRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Position findPositionByName(String name) {
        return Position.valueOf(name);
    }
}
