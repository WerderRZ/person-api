package com.werdersoft.personapi.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDTO> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id).toPersonDTO();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO createPerson(@RequestBody Person person) {
        return personService.createPerson(person).toPersonDTO();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePersonById(@PathVariable Long id, @RequestBody Person person) {
        return personService.updatePersonById(id, person).toPersonDTO();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonById(@PathVariable Long id) {
        personService.deletePersonById(id);
    }

}
