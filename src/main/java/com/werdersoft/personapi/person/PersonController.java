package com.werdersoft.personapi.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
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
        return personService.getPersonById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO createPerson(@Valid @RequestBody PersonDTO personDTO) {
        return personService.createPerson(personDTO);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePersonById(@PathVariable Long id, @Valid @RequestBody PersonDTO personDTO) {
        return personService.updatePersonById(id, personDTO);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonById(@PathVariable Long id) {
        personService.deletePersonById(id);
    }

}
