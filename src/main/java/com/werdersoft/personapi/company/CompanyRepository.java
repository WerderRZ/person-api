package com.werdersoft.personapi.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT comp FROM Company comp WHERE comp.id in (:companiesIds)")
    Set<Company> findCompaniesByCompaniesIds(List<UUID> companiesIds);

}
