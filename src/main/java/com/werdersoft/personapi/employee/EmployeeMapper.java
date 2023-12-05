package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.person.PersonServiceImpl;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionServiceImpl;
import com.werdersoft.personapi.util.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class EmployeeMapper {

    @Autowired
    protected PersonServiceImpl personService;

    @Autowired
    protected SubdivisionServiceImpl subdivisionService;

    @Mappings({
            @Mapping(source = "personId", target = "person"),
            @Mapping(source = "subdivisionId", target = "subdivision")
    })
    public abstract Employee toEmployee(EmployeeDTO employeeDTO);

    @Mappings({
            @Mapping(source = "person", target = "personId"),
            @Mapping(source = "subdivision", target = "subdivisionId")
    })
    public abstract EmployeeDTO toEmployeeDTO(Employee employee);

    public Person mapPersonIdToPerson(Long id) {
         return personService.findPersonById(id);
    }

    public Subdivision mapSubdivisionIdToSubdivision(Long id) {
        return subdivisionService.findSubversionById(id);
    }

    public Long mapPersonToPersonId(Person person) {
        return Utils.mapEntityToEntityId(person);
    }

    public Long mapSubdivisionToSubdivisionId(Subdivision subdivision) {
        return Utils.mapEntityToEntityId(subdivision);
    }

}