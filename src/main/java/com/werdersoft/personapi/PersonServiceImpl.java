package com.werdersoft.personapi;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import static com.werdersoft.personapi.Utils.*;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @Override
    public List<Person> getAllPersons() {
        LOGGER.info("Entering getAllPersons method");
        return toList(personRepository.findAll());
    }

    @Override
    public Person getPersonById(Long id) {
        LOGGER.info("Entering getPersonById method");
        return findPersonById(id);
    }

    @Override
    public Person createPerson(Person person) {
        LOGGER.info("Entering createPerson method");
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonById(Long id, Person person) {
        LOGGER.info("Entering updatePersonById method");
        Person findedPerson = findPersonById(id);
        findedPerson.setName(person.getName());
        findedPerson.setAge(person.getAge());
        return personRepository.save(findedPerson);
    }

    @Override
    public void deletePersonById(Long id) {
        LOGGER.info("Entering deletePersonById method");
        Person findedPerson = findPersonById(id);
        personRepository.delete(findedPerson);
    }

    private Person findPersonById(Long id) {
        LOGGER.info("Entering findPersonById method");
        return toValue(personRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
