package com.werdersoft.personapi.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.werdersoft.personapi.util.Utils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PersonDataLoading personDataLoading;

    @Override
    public List<PersonDTO> getAllPersons() {
        log.debug("Entering getAllPersons method");
        return getStream(personRepository.findAll())
                .map(personMapper::toPersonDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO getPersonById(UUID id) {
        log.debug("Entering getPersonById method");
        return personMapper.toPersonDTO(findPersonById(id));
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        log.debug("Entering createPerson method");
        return personMapper.toPersonDTO(personRepository.save(personMapper.toPerson(personDTO)));
    }

    @Override
    public PersonDTO updatePersonById(UUID id, PersonDTO personDTO) {
        log.debug("Entering updatePersonById method");
        Person findedPerson = findPersonById(id);
        findedPerson.setName(personDTO.getName());
        findedPerson.setAge(personDTO.getAge());
        return personMapper.toPersonDTO(personRepository.save(findedPerson));
    }

    @Override
    public void deletePersonById(UUID id) {
        log.debug("Entering deletePersonById method");
        Person findedPerson = findPersonById(id);
        personRepository.delete(findedPerson);
    }

    @Override
    public List<PersonDTO> updatePersonsFromSiteOutSystem() {
        List<Integer> existingIds = personRepository.findPersonsWhereExternalIdIsFilled();
        //TODO: Маппинг для преобразования DTO от API в Person
        List<Person> persons = personDataLoading.loadAllPersons(existingIds);
        //TODO: За один раз сохранять данные
        return persons.stream()
                .map(person -> personMapper.toPersonDTO(personRepository.save(person)))
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO downloadPersonByExternalId(Integer externalId) {
        Person findedPerson = personRepository.findPersonByExternalID(externalId);
        //TODO: Заменить на Optional
        return personMapper.toPersonDTO(
                Objects.requireNonNullElseGet(
                        findedPerson, () -> personRepository.save(personDataLoading.loadPersonByID(externalId))));
    }

    public Person findPersonById(UUID id) {
        log.debug("Entering findPersonById method");
        return toValue(personRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
