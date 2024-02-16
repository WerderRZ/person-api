package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.person.PersonServiceImpl;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PersonServiceImpl personService;
    @Lazy
    private final SubdivisionServiceImpl subdivisionService;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(UUID id) {
        return employeeMapper.toEmployeeDTO(findEmployeeById(id));
    }

    public Set<Employee> findEmployeesByEmployeesIds(List<UUID> employeesIds) {
        return employeeRepository.findEmployeesByEmployeesIds(employeesIds);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Person person = personService.findPersonById(employeeDTO.getPersonId());
        Subdivision subdivision = subdivisionService.findSubdivisionById(employeeDTO.getSubdivisionId());
        return employeeMapper.toEmployeeDTO(employeeRepository
                .save(employeeMapper.toEmployee(employeeDTO, person, subdivision)));
    }

    public Employee findEmployeeById(UUID id) {
        return toValue(employeeRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}