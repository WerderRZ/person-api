package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.enums.Position;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeDTO extends BaseDTO {
    private Position position;
    private BigDecimal salary;
    private Long personId;
    private Long subdivisionId;
}
