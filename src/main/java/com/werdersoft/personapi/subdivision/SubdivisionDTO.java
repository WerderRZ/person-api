package com.werdersoft.personapi.subdivision;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubdivisionDTO {
    private Long id;
    private String name;
    private List<Long> employee_ids;
    private List<Long> company_ids;
}
