package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class SubdivisionDTO extends BaseDTO {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, message = "Name should have at least 1 character")
    private String name;

    private List<Long> employeesIds;

    private List<Long> companiesIds;
}
