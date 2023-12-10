package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.company.CompanyServiceImpl;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.employee.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class SubdivisionServiceImpl implements SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;
    private final SubdivisionMapper subdivisionMapper;
    private CompanyServiceImpl companyService;
    private EmployeeServiceImpl employeeService;

    @Override
    public List<SubdivisionDTO> getAllSubdivisions() {
        return StreamSupport.stream(subdivisionRepository.findAll().spliterator(), false)
                .map(subdivisionMapper::toSubdivisionDTO).collect(Collectors.toList());
    }

    @Override
    public SubdivisionDTO getSubdivisionById(UUID id) {
        return subdivisionMapper.toSubdivisionDTO(findSubdivisionById(id));
    }

    @Override
    public SubdivisionDTO createSubdivision(SubdivisionDTO subdivisionDTO) {
        Set<Company> companies = mapCompaniesIdsToCompanies(subdivisionDTO.getCompaniesIds());
        Set<Employee> employees = mapEmployeesIdsToEmployees(subdivisionDTO.getEmployeesIds());
        return subdivisionMapper.toSubdivisionDTO(subdivisionRepository
                .save(subdivisionMapper.toSubdivision(subdivisionDTO, companies, employees)));
    }

    public Subdivision findSubdivisionById(UUID id) {
        return toValue(subdivisionRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Set<Company> mapCompaniesIdsToCompanies(List<UUID> companiesIds) {
        if (companiesIds != null) {
            return companiesIds.stream()
                    .map(companyService::findCompanyById)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

    private Set<Employee> mapEmployeesIdsToEmployees(List<UUID> employeesIds) {
        if (employeesIds != null) {
            return employeesIds.stream()
                    .map(employeeService::findEmployeeById)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }

    @Autowired
    public void setCompanyService(@Lazy CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setEmployeeService(@Lazy EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }
}
