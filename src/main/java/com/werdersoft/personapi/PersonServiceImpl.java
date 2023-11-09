package com.werdersoft.personapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import static com.werdersoft.personapi.Utils.*;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getAllPersons() {
        return toList(personRepository.findAll());
    }

    @Override
    public Person getPersonById(Long id) {
        return findPersonById(id);
    }

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person updatePersonById(Long id, Person person) {
        Person findedPerson = findPersonById(id);
        findedPerson.setName(person.getName());
        findedPerson.setAge(person.getAge());
        return personRepository.save(findedPerson);
    }

    @Override
    public void deletePersonById(Long id) {
        Person findedPerson = findPersonById(id);
        personRepository.delete(findedPerson);
    }

    private Person findPersonById(Long id) {
        return toValue(personRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
