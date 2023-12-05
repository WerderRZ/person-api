package com.werdersoft.personapi.person;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getAllPersons();

    PersonDTO getPersonById(Long id);

    PersonDTO createPerson(PersonDTO personDTO);

    PersonDTO updatePersonById(Long id, PersonDTO personDTO);

    void deletePersonById(Long id);

}
