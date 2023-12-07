package com.werdersoft.personapi.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO createCompany(@Valid @RequestBody Company company) {
        return companyService.createCompany(company);
    }

}
