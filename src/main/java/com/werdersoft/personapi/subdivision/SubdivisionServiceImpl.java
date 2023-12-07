package com.werdersoft.personapi.subdivision;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.werdersoft.personapi.util.Utils.toValue;

@Service
@RequiredArgsConstructor
public class SubdivisionServiceImpl implements SubdivisionService {

    private final SubdivisionRepository subdivisionRepository;
    private final SubdivisionMapper subdivisionMapper;

    @Override
    public List<SubdivisionDTO> getAllSubdivisions() {
        List<SubdivisionDTO> subdivisionDTOList = new ArrayList<>();
        subdivisionRepository.findAll().forEach(subdivision -> subdivisionDTOList.add(subdivisionMapper.toSubdivisionDTO(subdivision)));
        return subdivisionDTOList;
    }

    @Override
    public SubdivisionDTO getSubdivisionById(UUID id) {
        return subdivisionMapper.toSubdivisionDTO(findSubversionById(id));
    }

    @Override
    public SubdivisionDTO createSubdivision(SubdivisionDTO subdivisionDTO) {
        return subdivisionMapper.toSubdivisionDTO(subdivisionRepository
                .save(subdivisionMapper.toSubdivision(subdivisionDTO)));
    }

    public Subdivision findSubversionById(UUID id) {
        return toValue(subdivisionRepository.findById(id), new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
