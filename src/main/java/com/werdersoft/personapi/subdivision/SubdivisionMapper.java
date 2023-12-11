package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.employee.Employee;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SubdivisionMapper {

    @Mapping(target = "employeesIds", source = "employees")
    @Mapping(target = "companiesIds", source = "companies")
    public abstract SubdivisionDTO toSubdivisionDTO(Subdivision subdivision);

    @Mapping(target = "companies", source = "companies")
    @Mapping(target = "employees", source = "employees")
    public abstract Subdivision toSubdivision(SubdivisionDTO subdivisionDTO,
                                              Set<Company> companies, Set<Employee> employees);

    public abstract List<UUID> toSubdivisionsIds(Set<Subdivision> subdivisions);

    public UUID mapSubdivisionToId(Subdivision subdivision) {
        return subdivision.getId();
    }

    public abstract List<UUID> toCompaniesIds(Set<Company> companies);

    public UUID mapCompanyToId(Company company) {
        return company.getId();
    }

    public abstract List<UUID> toEmployeesIds(Set<Employee> employees);

    public UUID mapEmployeeToId(Employee employee) {
        return employee.getId();
    }

}