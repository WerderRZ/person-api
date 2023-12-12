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
import java.util.stream.StreamSupport;

import static com.werdersoft.personapi.util.Utils.mapEntityToEntitiesToEntitiesIds;
import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    @Lazy
    private final SubdivisionServiceImpl subdivisionService;

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
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
        if (companiesIds != null) {
            return companyRepository.findCompaniesByCompaniesIds(companiesIds);
        }
        return null;
    }

    private CompanyDTO mapCompanyToCompanyDTO(Company company) {
        return companyMapper.toCompanyDTO(company, mapEntityToEntitiesToEntitiesIds(company.getSubdivisions()));
    }
}
