package com.werdersoft.personapi.company;

import com.datical.liquibase.ext.storedlogic.StoredLogicComparator;
import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.subdivision.Subdivision;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "company")
public class Company extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    private Set<Subdivision> subdivisions;

    public CompanyDTO toCompanyDTO() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(getId());
        companyDTO.setName(getName());
        List<Long> subdivisionsList = new ArrayList<>();
        subdivisions.forEach(subdivision -> subdivisionsList.add(subdivision.getId()));
        companyDTO.setSubdivision_ids(subdivisionsList);
        return companyDTO;
    }

}
