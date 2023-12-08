package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.util.Utils;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", source = "person")
    @Mapping(target = "subdivision", source = "subdivision")
    public abstract Employee toEmployee(EmployeeDTO employeeDTO, Person person, Subdivision subdivision);

    @Mapping(target = "personId", source = "person")
    @Mapping(target = "subdivisionId", source = "subdivision")
    public abstract EmployeeDTO toEmployeeDTO(Employee employee);

    public UUID mapPersonToPersonId(Person person) {
        return Utils.mapEntityToEntityId(person);
    }

    public UUID mapSubdivisionToSubdivisionId(Subdivision subdivision) {
        return Utils.mapEntityToEntityId(subdivision);
    }

}