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
                .map(companyMapper::toCompanyDTO).collect(Collectors.toList());
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Set<Subdivision> subdivisions = mapSubdivisionsIdsToSubdivisions(companyDTO.getSubdivisionsIds());
        return companyMapper.toCompanyDTO(companyRepository.save(companyMapper.toCompany(companyDTO, subdivisions)));
    }

    public Company findCompanyById(UUID id) {
        return toValue(companyRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Set<Subdivision> mapSubdivisionsIdsToSubdivisions(List<UUID> subdivisionsIds) {
        if (subdivisionsIds != null) {
            return subdivisionsIds.stream()
                    .map(subdivisionService::findSubdivisionById)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
}
