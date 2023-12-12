package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.employee.Employee;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SubdivisionMapper {

    @Mapping(target = "companiesIds", source = "companiesIds")
    @Mapping(target = "employeesIds", source = "employeesIds")
    public abstract SubdivisionDTO toSubdivisionDTO(Subdivision subdivision,
                                                    List<UUID> companiesIds, List<UUID> employeesIds);

    @Mapping(target = "companies", source = "companies")
    @Mapping(target = "employees", source = "employees")
    public abstract Subdivision toSubdivision(SubdivisionDTO subdivisionDTO,
                                              Set<Company> companies, Set<Employee> employees);

}