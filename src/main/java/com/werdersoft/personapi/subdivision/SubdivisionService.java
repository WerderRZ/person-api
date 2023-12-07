package com.werdersoft.personapi.subdivision;

import java.util.List;
import java.util.UUID;

public interface SubdivisionService {
    List<SubdivisionDTO> getAllSubdivisions();

    SubdivisionDTO getSubdivisionById(UUID id);

    SubdivisionDTO createSubdivision(SubdivisionDTO subdivisionDTO);

}
