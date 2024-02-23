package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.employee.Employee;
import com.werdersoft.personapi.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "subdivision")
public class Subdivision extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "subdivision", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany
    @JoinTable(name="\"subdivisions-companies\"",
        joinColumns = @JoinColumn(name = "subdivision_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id")
    )
    private Set<Company> companies = new HashSet<>();

    public Subdivision() {}

    @Builder
    public Subdivision(UUID id, String name, Set<Employee> employees, Set<Company> companies) {
        super(id);
        this.name = name;
        this.employees = employees;
        this.companies = companies;
    }

}
