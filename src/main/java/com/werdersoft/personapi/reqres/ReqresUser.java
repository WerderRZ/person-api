package com.werdersoft.personapi.reqres;

import lombok.Data;

@Data
public class ReqresUser {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
