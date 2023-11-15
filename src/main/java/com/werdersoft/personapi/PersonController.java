package com.werdersoft.personapi;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Person getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person) {
        return personService.createPerson(person);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Person updatePersonById(@PathVariable Long id, @RequestBody Person person) {
        return personService.updatePersonById(id, person);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonById(@PathVariable Long id) {
        personService.deletePersonById(id);
    }

}
