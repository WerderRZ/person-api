package com.werdersoft.personapi.subdivision;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static com.werdersoft.personapi.util.Utils.toList;
import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class SubdivisionServiceImpl implements SubdivisionService {

    private final SubversionRepository subversionRepository;

    @Override
    public List<SubdivisionDTO> getAllSubdivisions() {
        List<SubdivisionDTO> subdivisionDTOList = new ArrayList<>();
        subversionRepository.findAll().forEach(subdivision -> subdivisionDTOList.add(subdivision.toSubdivisionDTO()));
        return subdivisionDTOList;
    }

    @Override
    public Subdivision getSubdivisionById(Long id) {
        return findSubversionById(id);
    }

    @Override
    public Subdivision createSubdivision(Subdivision subdivision) {
        return subversionRepository.save(subdivision);
    }

    @Override
    public Subdivision updateSubdivisionById(Long id, Subdivision subdivision) {
        Subdivision foundSubversion = findSubversionById(id);
        foundSubversion.setName(subdivision.getName());
        return subversionRepository.save(foundSubversion);
    }

    @Override
    public void deleteSubdivisionById(Long id) {
        Subdivision foundSubversion = findSubversionById(id);
        subversionRepository.delete(foundSubversion);
    }

    public Subdivision findSubversionById(Long id) {
        return toValue(subversionRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
