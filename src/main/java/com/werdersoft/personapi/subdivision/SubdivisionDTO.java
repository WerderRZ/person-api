package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubdivisionDTO extends BaseDTO {
    private String name;
    private List<Long> employeesIds;
    private List<Long> companiesIds;
}
