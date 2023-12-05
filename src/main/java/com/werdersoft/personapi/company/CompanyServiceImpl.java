package com.werdersoft.personapi.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> companyDTOList = new ArrayList<>();
        companyRepository.findAll().forEach(company -> companyDTOList.add(companyMapper.toCompanyDTO(company)));
        return companyDTOList;
    }

    @Override
    public CompanyDTO createCompany(Company company) {
        return companyMapper.toCompanyDTO(companyRepository.save(company));
    }

    public Company findCompanyById(Long id) {
        return toValue(companyRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
