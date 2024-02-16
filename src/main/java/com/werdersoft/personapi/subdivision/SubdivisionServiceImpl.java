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

import static com.werdersoft.personapi.util.Utils.*;

@Service
@RequiredArgsConstructor
public class SubdivisionServiceImpl implements SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;
    private final SubdivisionMapper subdivisionMapper;
    private CompanyServiceImpl companyService;
    private EmployeeServiceImpl employeeService;

    @Override
    public List<SubdivisionDTO> getAllSubdivisions() {
        return subdivisionRepository.findAll().stream()
                .map(this::mapSubdivisionTosubdivisionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubdivisionDTO getSubdivisionById(UUID id) {
        return mapSubdivisionTosubdivisionDTO(findSubdivisionById(id));
    }

    @Override
    public SubdivisionDTO createSubdivision(SubdivisionDTO subdivisionDTO) {
        Set<Company> companies = companyService.findCompaniesByCompaniesIds(subdivisionDTO.getCompaniesIds());
        Set<Employee> employees = employeeService.findEmployeesByEmployeesIds(subdivisionDTO.getEmployeesIds());
        return mapSubdivisionTosubdivisionDTO(subdivisionRepository
                .save(subdivisionMapper.toSubdivision(subdivisionDTO, companies, employees)));
    }

    public Subdivision findSubdivisionById(UUID id) {
        return toValue(subdivisionRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Set<Subdivision> findSubdivisionsBySubdivisionsIds(List<UUID> subdivisionsIds) {
        return subdivisionRepository.findSubdivisionsBySubdivisionsIds(subdivisionsIds);
    }

    private SubdivisionDTO mapSubdivisionTosubdivisionDTO(Subdivision subdivision) {
        return subdivisionMapper.toSubdivisionDTO(subdivision, mapEntityToEntitiesToEntitiesIds(
                subdivision.getCompanies()), mapEntityToEntitiesToEntitiesIds(subdivision.getEmployees()));
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
