package com.werdersoft.personapi.company;

import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static com.werdersoft.personapi.util.Utils.*;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    @Lazy
    private final SubdivisionServiceImpl subdivisionService;

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return getStream(companyRepository.findAll())
                .map(company -> companyMapper.toCompanyDTO(company, mapEntityToEntitiesToEntitiesIds(company.getSubdivisions())))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Set<Subdivision> subdivisions = subdivisionService.findSubdivisionsBySubdivisionsIds(companyDTO.getSubdivisionsIds());
        return mapCompanyToCompanyDTO(companyRepository.save(companyMapper.toCompany(companyDTO, subdivisions)));
    }

    public Company findCompanyById(UUID id) {
        return toValue(companyRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Set<Company> findCompaniesByCompaniesIds(List<UUID> companiesIds) {
        return companyRepository.findCompaniesByCompaniesIds(companiesIds);
    }

    private CompanyDTO mapCompanyToCompanyDTO(Company company) {
        return companyMapper.toCompanyDTO(company, mapEntityToEntitiesToEntitiesIds(company.getSubdivisions()));
    }
}
