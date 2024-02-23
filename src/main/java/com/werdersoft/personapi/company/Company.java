package com.werdersoft.personapi.company;

import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.enums.Region;
import com.werdersoft.personapi.subdivision.Subdivision;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private Set<Subdivision> subdivisions = new HashSet<>();

    public Company() {}

    @Builder
    public Company(UUID id, String name, Region region, Set<Subdivision> subdivisions) {
        super(id);
        this.name = name;
        this.region = region;
        this.subdivisions = subdivisions;
    }

}
