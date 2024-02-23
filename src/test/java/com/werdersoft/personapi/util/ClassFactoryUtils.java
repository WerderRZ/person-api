package com.werdersoft.personapi.util;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.company.CompanyDTO;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.employee.EmployeeDTO;
import com.werdersoft.personapi.enums.Position;
import com.werdersoft.personapi.enums.Region;
import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.person.PersonDTO;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionDTO;

import java.math.BigDecimal;
import java.util.*;

public class ClassFactoryUtils {

    public static PersonDTO newPersonDTO() {
        return PersonDTO.builder()
                .name("Sam")
                .age(22)
                .build();
    }

    public static Person newPerson() {
        return Person.builder()
                .name("Sam")
                .age(22)
                .build();
    }

    public static CompanyDTO newCompanyDTO() {
        return CompanyDTO.builder()
                .name("Werdersoft")
                .region(Region.ASIA)
                .subdivisionsIds(new ArrayList<>())
                .build();
    }

    public static Company newCompany() {
        return Company.builder()
                .name("Werdersoft")
                .region(Region.ASIA)
                .build();
    }

    public static SubdivisionDTO newSubdivisionDTO(UUID companyId) {
        return SubdivisionDTO.builder()
                .name("IT")
                .companiesIds(List.of(companyId))
                .employeesIds(new ArrayList<>())
                .build();
    }

    public static Subdivision newSubdivision() {
        return Subdivision.builder()
                .name("IT")
                .build();
    }

    public static Map<UUID, UUID> newSubdivisionCompanyMap(UUID companyId, UUID subdivisionId) {
        return Map.of(companyId, subdivisionId);
    }

    public static EmployeeDTO newEmployeeDTO(UUID personId, UUID subdivisionId) {
        return EmployeeDTO.builder()
                .position(Position.MANAGER)
                .salary(new BigDecimal(300))
                .personId(personId)
                .subdivisionId(subdivisionId)
                .build();
    }

}
