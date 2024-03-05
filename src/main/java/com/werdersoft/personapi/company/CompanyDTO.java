package com.werdersoft.personapi.company;

import com.werdersoft.personapi.dto.BaseDTO;
import com.werdersoft.personapi.enums.Region;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyDTO extends BaseDTO {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 1, message = "Name should have at least 1 character")
    private String name;

    @NotNull(message = "Region should not be empty")
    private Region region;

    private List<UUID> subdivisionsIds;

    @Builder
    public CompanyDTO(UUID id, String name, Region region, List<UUID> subdivisionsIds) {
        super(id);
        this.name = name;
        this.region = region;
        this.subdivisionsIds = subdivisionsIds;
    }
}
