package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.subdivision.Subdivision;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", source = "person")
    @Mapping(target = "subdivision", source = "subdivision")
    public abstract Employee toEmployee(EmployeeDTO employeeDTO, Person person, Subdivision subdivision);

    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "subdivisionId", source = "subdivision.id")
    public abstract EmployeeDTO toEmployeeDTO(Employee employee);

}