package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.enums.Position;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeDTO extends BaseDTO {

    @NotNull(message = "Field 'position' cannot be empty")
    private Position position;

    private BigDecimal salary;

    @NotNull(message = "Field 'personId' cannot be empty")
    private Long personId;

    @NotNull(message = "Field 'subdivisionId' cannot be empty")
    private Long subdivisionId;
}
