package com.werdersoft.personapi.company;

import java.util.List;

public interface CompanyService {

    List<CompanyDTO> getAllCompanies();

    Company createCompany(Company company);

}
