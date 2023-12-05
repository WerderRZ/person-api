package com.werdersoft.personapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseDTO {
    private Long id;
}
