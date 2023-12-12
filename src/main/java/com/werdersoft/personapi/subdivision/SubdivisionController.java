package com.werdersoft.personapi.subdivision;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subdivisions")
@RequiredArgsConstructor
@Validated
public class SubdivisionController {

    private final SubdivisionService subdivisionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SubdivisionDTO> getAllSubdivisions() {
        return subdivisionService.getAllSubdivisions();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubdivisionDTO getSubdivisionById(@PathVariable UUID id) {
        return subdivisionService.getSubdivisionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubdivisionDTO createSubdivision(@Valid @RequestBody SubdivisionDTO subdivisionDTO) {
        return subdivisionService.createSubdivision(subdivisionDTO);
    }

}
