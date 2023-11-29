package com.werdersoft.personapi.company;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyDTO {
    private Long id;
    private String name;
    private List<Long> subdivision_ids;
}
