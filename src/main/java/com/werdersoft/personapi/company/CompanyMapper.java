package com.werdersoft.personapi.company;

import com.werdersoft.personapi.entity.BaseEntity;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionService;
import com.werdersoft.personapi.subdivision.SubdivisionServiceImpl;
import com.werdersoft.personapi.util.Utils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CompanyMapper {

//    @Autowired
//    protected SubdivisionServiceImpl subdivisionService;

    @Mapping(source = "subdivisions", target = "subdivisionsIds")
    public abstract CompanyDTO toCompanyDTO(Company company);

//    @Mapping(source = "subdivisionsIds", target = "subdivisions")
//    public abstract Company toCompany(CompanyDTO companyDTO);

    public List<Long> mapSubdivisionsToSubdivisionsIds(Set<Subdivision> subdivisions) {
        return Utils.mapEntitiesToEntitiesIds(subdivisions);
    }

//    public Set<Subdivision> mapSubdivisionsIdsToSubdivisions(List<Long> subdivisionsIds) {
//        Set<Subdivision>  subdivisionSet = new HashSet<>();
//        if (subdivisionsIds != null) {
//            subdivisionsIds.forEach(id -> subdivisionSet.add(subdivisionService.findSubversionById(id)));
//        }
//        return subdivisionSet;
//    }

}
