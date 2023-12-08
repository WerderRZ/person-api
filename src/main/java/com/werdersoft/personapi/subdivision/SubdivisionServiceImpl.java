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
        List<SubdivisionDTO> subdivisionDTOList = new ArrayList<>();
        subdivisionRepository.findAll().forEach(subdivision -> subdivisionDTOList.add(subdivisionMapper.toSubdivisionDTO(subdivision)));
        return subdivisionDTOList;
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
        Set<Company> companySet = new HashSet<>();
        if (companiesIds != null) {
            companiesIds.forEach(id -> companySet.add(companyService.findCompanyById(id)));
        }
        return companySet;
    }

    private Set<Employee> mapEmployeesIdsToEmployees(List<UUID> employeesIds) {
        Set<Employee> employeeSet = new HashSet<>();
        if (employeesIds != null) {
            employeesIds.forEach(id -> employeeSet.add(employeeService.findEmployeeById(id)));
        }
        return employeeSet;
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
