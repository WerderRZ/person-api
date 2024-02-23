package com.werdersoft.personapi.person;

import com.werdersoft.personapi.reqres.ReqresUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonMapper {

    Person toPerson(PersonDTO personDTO);
    PersonDTO toPersonDTO(Person person);

    @Mapping(target = "name", source = "first_name")
    @Mapping(target = "externalID", source = "id")
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "id", ignore = true)
    Person toPersonFromReqresUser(ReqresUser reqresUser);

    List<Person> toPersonsListFromReqresUsersList(List<ReqresUser> reqresUsersList);

}
