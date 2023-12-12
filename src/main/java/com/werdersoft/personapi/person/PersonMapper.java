package com.werdersoft.personapi.person;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    Person toPerson(PersonDTO personDTO);
    PersonDTO toPersonDTO(Person person);

}
