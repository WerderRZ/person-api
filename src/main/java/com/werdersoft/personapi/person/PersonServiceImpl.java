package com.werdersoft.personapi.person;

import com.werdersoft.personapi.reqres.ReqresService;
import com.werdersoft.personapi.reqres.ReqresUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.werdersoft.personapi.util.Utils.*;
import static com.werdersoft.personapi.util.Utils.getStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final ReqresService reqresService;

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
        List<ReqresUser> reqresUsers = reqresService.getAllPersons().stream()
                .filter(user -> !existingIds.contains(user.getId()))
                .collect(Collectors.toList());

        Iterable<Person> persons = personRepository.saveAll(personMapper.toPersonsListFromReqresUsersList(reqresUsers));

        return getStream(persons)
                .map(personMapper::toPersonDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO downloadPersonByExternalId(Integer externalId) {
        PersonDTO personDTO = null;
        if (personRepository.findPersonByExternalID(externalId).isEmpty()) {
            ReqresUser reqresUser = reqresService.getPersonById(externalId);
            personDTO = personMapper.toPersonDTO(
                            personRepository.save(
                                personMapper.toPersonFromReqresUser(reqresUser)));
        }
        return personDTO;
    }

    public Person findPersonById(UUID id) {
        log.debug("Entering findPersonById method");
        return toValue(personRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
