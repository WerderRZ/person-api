package com.werdersoft.personapi.company;

import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.enums.Region;
import com.werdersoft.personapi.subdivision.Subdivision;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "company")
public class Company extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name  = "region")
    @Enumerated(EnumType.STRING)
    private Region region;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    private Set<Subdivision> subdivisions;

}
