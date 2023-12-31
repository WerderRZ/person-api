package com.werdersoft.personapi.company;

import com.werdersoft.personapi.subdivision.Subdivision;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CompanyMapper {

    @Mapping(target = "subdivisionsIds", source = "subdivisionsIds")
    public abstract CompanyDTO toCompanyDTO(Company company, List<UUID> subdivisionsIds);

    @Mapping(target = "subdivisions", source = "subdivisions")
    public abstract Company toCompany(CompanyDTO companyDTO, Set<Subdivision> subdivisions);

}
