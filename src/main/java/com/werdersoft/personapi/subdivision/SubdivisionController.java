package com.werdersoft.personapi.subdivision;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subdivisions")
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService subdivisionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SubdivisionDTO> getAllSubdivisions() {
        return subdivisionService.getAllSubdivisions();
    }

//    Subdivision getSubdivisionById(Long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubdivisionDTO createSubdivision(@RequestBody Subdivision subdivision) {
        return subdivisionService.createSubdivision(subdivision).toSubdivisionDTO();
    }
//
//    Subdivision updateSubdivisionById(Long id, Subdivision subdivision);
//
//    void deleteSubdivisionById(Long id);
}
