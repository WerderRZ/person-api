package com.werdersoft.personapi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public abstract class BaseDTO {

    private UUID id;

    public BaseDTO() {
    }

    public BaseDTO(UUID id) {
        this.id = id;
    }

}
