package com.werdersoft.personapi.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import static com.werdersoft.personapi.util.Utils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public List<PersonDTO> getAllPersons() {
        log.debug("Entering getAllPersons method");
        List<PersonDTO> personDTOList = new ArrayList<>();
        personRepository.findAll().forEach(person -> personDTOList.add(person.toPersonDTO()));
        return personDTOList;
    }

    @Override
    public Person getPersonById(Long id) {
        log.debug("Entering getPersonById method");
        return findPersonById(id);
    }

    @Override
    public Person createPerson(Person person) {
        log.debug("Entering createPerson method");
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonById(Long id, Person person) {
        log.debug("Entering updatePersonById method");
        Person findedPerson = findPersonById(id);
        findedPerson.setName(person.getName());
        findedPerson.setAge(person.getAge());
        return personRepository.save(findedPerson);
    }

    @Override
    public void deletePersonById(Long id) {
        log.debug("Entering deletePersonById method");
        Person findedPerson = findPersonById(id);
        personRepository.delete(findedPerson);
    }

    public Person findPersonById(Long id) {
        log.debug("Entering findPersonById method");
        return toValue(personRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}