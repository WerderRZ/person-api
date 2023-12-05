package com.werdersoft.personapi.subdivision;

import java.util.List;

public interface SubdivisionService {
    List<SubdivisionDTO> getAllSubdivisions();

    SubdivisionDTO getSubdivisionById(Long id);

    SubdivisionDTO createSubdivision(SubdivisionDTO subdivisionDTO);

}
