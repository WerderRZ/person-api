package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "subdivision")
public class Subdivision extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "subdivision", cascade = CascadeType.ALL)
    private Set<Employee> employees;

    @ManyToMany
    @JoinTable(name="\"subdivisions-companies\"",
        joinColumns = @JoinColumn(name = "subdivision_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id")
    )
    private Set<Company> companies;

    public SubdivisionDTO toSubdivisionDTO() {
        SubdivisionDTO subdivisionDTO = new SubdivisionDTO();
        subdivisionDTO.setId(getId());
        subdivisionDTO.setName(getName());

        List<Long> employeeIds = new ArrayList<>();
        employees.forEach(employee -> employeeIds.add(employee.getId()));
        subdivisionDTO.setEmployee_ids(employeeIds);

        List<Long> companyIds = new ArrayList<>();
        companies.forEach(company -> companyIds.add(company.getId()));
        subdivisionDTO.setCompany_ids(companyIds);

        return subdivisionDTO;
    }

}