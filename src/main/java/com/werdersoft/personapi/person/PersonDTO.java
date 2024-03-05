package com.werdersoft.personapi.person;

import com.werdersoft.personapi.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonDTO extends BaseDTO {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 1, message = "Name should have at least 1 character")
    private String name;

    @Min(value = 0, message = "Age should be greater than 0")
    private Integer age;
  
    private Integer externalID;

    private String email;

    @Builder
    public PersonDTO(UUID id, String name, Integer age, Integer externalID, String email) {
        super(id);
        this.name = name;
        this.age = age;
        this.externalID = externalID;
        this.email = email;
    }

}
