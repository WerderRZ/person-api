package com.werdersoft.personapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class BaseDTO {
    private UUID id;
}
