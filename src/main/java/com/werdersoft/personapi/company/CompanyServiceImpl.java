package com.werdersoft.personapi.company;

import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    @Lazy
    private final SubdivisionServiceImpl subdivisionService;

//    public CompanyServiceImpl(CompanyRepository companyRepository,
//                              CompanyMapper companyMapper,
//                              @Lazy SubdivisionServiceImpl subdivisionService) {
//        this.companyRepository = companyRepository;
//        this.companyMapper = companyMapper;
//        this.subdivisionService = subdivisionService;
//    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> companyDTOList = new ArrayList<>();
        companyRepository.findAll().forEach(company -> companyDTOList.add(companyMapper.toCompanyDTO(company)));
        return companyDTOList;
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
        Set<Subdivision> subdivisionSet = new HashSet<>();
        if (subdivisionsIds != null) {
            subdivisionsIds.forEach(id -> subdivisionSet.add(subdivisionService.findSubdivisionById(id)));
        }
        return subdivisionSet;
    }
}
