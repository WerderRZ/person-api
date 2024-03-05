package com.werdersoft.personapi.reqres;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqresSingleUserDTO {
    private ReqresUser data;
    private ReqresSupportDTO support;
}
