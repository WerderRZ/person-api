package com.werdersoft.personapi.person;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    List<PersonDTO> getAllPersons();

    PersonDTO getPersonById(UUID id);

    PersonDTO createPerson(PersonDTO personDTO);

    PersonDTO updatePersonById(UUID id, PersonDTO personDTO);

    void deletePersonById(UUID id);

}
