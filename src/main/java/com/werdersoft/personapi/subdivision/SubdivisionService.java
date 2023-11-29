package com.werdersoft.personapi.subdivision;

import java.util.List;

public interface SubdivisionService {
    List<SubdivisionDTO> getAllSubdivisions();

    Subdivision getSubdivisionById(Long id);

    Subdivision createSubdivision(Subdivision subdivision);

    Subdivision updateSubdivisionById(Long id, Subdivision subdivision);

    void deleteSubdivisionById(Long id);
}
