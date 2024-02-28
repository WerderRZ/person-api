package com.werdersoft.personapi.reqres;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqresUser {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
