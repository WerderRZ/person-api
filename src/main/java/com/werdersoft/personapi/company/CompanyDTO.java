package com.werdersoft.personapi.company;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.enums.Region;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyDTO extends BaseDTO {
    private String name;
    private Region region;
    private List<Long> subdivisionsIds;
}
