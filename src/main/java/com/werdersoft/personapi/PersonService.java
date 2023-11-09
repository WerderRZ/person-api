package com.werdersoft.personapi;

import java.util.List;

public interface PersonService {

    List<Person> getAllPersons();

    Person getPersonById(Long id);

    Person createPerson(Person person);

    Person updatePersonById(Long id, Person person);

    void deletePersonById(Long id);

}
