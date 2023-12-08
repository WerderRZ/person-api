package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubdivisionDTO extends BaseDTO {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 1, message = "Name should have at least 1 character")
    private String name;

    private List<UUID> employeesIds;

    private List<UUID> companiesIds;
}
