package com.werdersoft.personapi.person;

import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.reqres.ReqresClient;
import com.werdersoft.personapi.reqres.ReqresUser;
import com.werdersoft.personapi.util.BaseNativeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.werdersoft.personapi.util.Utils.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final ReqresClient reqresClient;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PersonDTO> getAllPersons() {
        log.debug("Entering getAllPersons method");
        return personRepository.findAll().stream()
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
        Person person = personMapper.toPerson(personDTO);
        return personMapper.toPersonDTO(personRepository.save(person));
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
        List<ReqresUser> reqresUsers = reqresClient.getAllPersons().stream()
                .filter(user -> !existingIds.contains(user.getId()))
                .collect(Collectors.toList());

        List<Person> preparedPersons = personMapper.toPersonsListFromReqresUsersList(reqresUsers);
        preparedPersons.forEach(person -> person.setId(UUID.randomUUID()));

        if (!preparedPersons.isEmpty()) {
            String query = "INSERT INTO person (id, name, age, external_id, email) VALUES (?,?,?,?,?)";
            BiConsumer<PreparedStatement, Person> biConsumer = (preparedStatement, person) -> {
                try {
                    preparedStatement.setObject(1, person.getId());
                    preparedStatement.setString(2, person.getName());
                    preparedStatement.setInt(3, person.getAge() == null ? 0 : person.getAge());
                    preparedStatement.setInt(4, person.getExternalID());
                    preparedStatement.setString(5, person.getEmail());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            };
            int savedRows = BaseNativeQueryRepository.batchProcess(entityManager, preparedPersons, query, biConsumer);
            if (savedRows != preparedPersons.size()) {
                throw new RuntimeException("not saved in DB rows: " + (preparedPersons.size() - savedRows));
            }
        }

        List<Person> persons = personRepository.findPersonsByIds(
                preparedPersons.stream()
                .map(BaseEntity::getId)
                .toList());

        return persons.stream()
                .map(personMapper::toPersonDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO downloadPersonByExternalId(Integer externalId) {
        Optional<Person> foundPerson = personRepository.findPersonByExternalID(externalId);
        if (foundPerson.isPresent()) {
            return personMapper.toPersonDTO(foundPerson.get());
        } else {
            ReqresUser reqresUser = reqresClient.getPersonById(externalId);
            return personMapper.toPersonDTO(personRepository.save(
                    personMapper.toPersonFromReqresUser(reqresUser)));
        }
    }

    public Person findPersonById(UUID id) {
        log.debug("Entering findPersonById method");
        return toValue(personRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
