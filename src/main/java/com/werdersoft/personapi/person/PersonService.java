package com.werdersoft.personapi.person;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getAllPersons();

    Person getPersonById(Long id);

    Person createPerson(Person person);

    Person updatePersonById(Long id, Person person);

    void deletePersonById(Long id);

}
