package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.enums.Position;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmployeeDTO {
    private Position position;
    private BigDecimal salary;
    private Long person_id;
    private Long subdivision_id;
}
