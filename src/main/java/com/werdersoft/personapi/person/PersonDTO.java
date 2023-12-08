package com.werdersoft.personapi.person;

import com.werdersoft.personapi.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PersonDTO extends BaseDTO {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 1, message = "Name should have at least 1 character")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    private Integer age;

}
