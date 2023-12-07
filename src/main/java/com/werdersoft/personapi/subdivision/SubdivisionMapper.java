package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.company.CompanyServiceImpl;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.employee.EmployeeServiceImpl;
import com.werdersoft.personapi.util.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SubdivisionMapper {

//    @Autowired
//    protected EmployeeServiceImpl employeeService;
    @Autowired
    protected CompanyServiceImpl companyService;

    @Mappings({
        @Mapping(source = "employees", target = "employeesIds"),
        @Mapping(source = "companies", target = "companiesIds")
    })
    public abstract SubdivisionDTO toSubdivisionDTO(Subdivision subdivision);

    @Mappings({
    //        @Mapping(source = "employeesIds", target = "employees"),
            @Mapping(source = "companiesIds", target = "companies")
    })
    public abstract Subdivision toSubdivision(SubdivisionDTO subdivisionDTO);

    public List<UUID> mapEmployeesToEmployeesIds(Set<Employee> employees) {
        return Utils.mapEntitiesToEntitiesIds(employees);
    }

    public List<UUID> mapCompaniesToCompaniesIds(Set<Company> companies) {
        return Utils.mapEntitiesToEntitiesIds(companies);
    }

//    public Set<Employee> mapEmployeesIdsToEmployees(List<UUID> employeesIds) {
//        Set<Employee> employeeSet = new HashSet<>();
//        if (employeesIds != null) {
//            employeesIds.forEach(id -> employeeSet.add(employeeService.findEmployeeById(id)));
//        }
//        return employeeSet;
//    }

    public Set<Company> mapCompaniesIdsToCompanies(List<UUID> companiesIds) {
        Set<Company> companySet = new HashSet<>();
        if (companiesIds != null) {
            companiesIds.forEach(id -> companySet.add(companyService.findCompanyById(id)));
        }
        return companySet;
    }

}
