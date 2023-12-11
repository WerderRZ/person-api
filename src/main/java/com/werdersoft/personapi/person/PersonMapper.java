package com.werdersoft.personapi.person;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    Person toPerson(PersonDTO personDTO);
    PersonDTO toPersonDTO(Person person);

    default UUID mapPersonToId(Person person) {
        return person.getId();
    }
}
