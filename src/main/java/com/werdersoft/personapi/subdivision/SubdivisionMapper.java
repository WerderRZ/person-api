package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.util.Utils;
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

    public List<UUID> mapEmployeesToEmployeesIds(Set<Employee> employees) {
        return Utils.mapEntitiesToEntitiesIds(employees);
    }

    public List<UUID> mapCompaniesToCompaniesIds(Set<Company> companies) {
        return Utils.mapEntitiesToEntitiesIds(companies);
    }

}