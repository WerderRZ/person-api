package com.werdersoft.personapi.person;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private Long id;
    private String name;
    private Integer age;
    private Long employee_id;

}
