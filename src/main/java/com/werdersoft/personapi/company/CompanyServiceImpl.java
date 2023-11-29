package com.werdersoft.personapi.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.werdersoft.personapi.util.Utils.toList;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;

    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> companyDTOList = new ArrayList<>();
        companyRepository.findAll().forEach(company -> companyDTOList.add(company.toCompanyDTO()));
        return companyDTOList;
    }

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }
}
