package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.enums.Position;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeDTO extends BaseDTO {

    @NotNull(message = "Field 'position' cannot be empty")
    private Position position;

    private BigDecimal salary;

    @NotNull(message = "Field 'personId' cannot be empty")
    private UUID personId;

    @NotNull(message = "Field 'subdivisionId' cannot be empty")
    private UUID subdivisionId;

    @Builder
    public EmployeeDTO(UUID id, Position position, BigDecimal salary, UUID personId, UUID subdivisionId) {
        super(id);
        this.position = position;
        this.salary = salary;
        this.personId = personId;
        this.subdivisionId = subdivisionId;
    }
}
