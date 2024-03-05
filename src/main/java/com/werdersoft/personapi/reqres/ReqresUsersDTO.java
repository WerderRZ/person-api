package com.werdersoft.personapi.reqres;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ReqresUsersDTO {
    private Integer page;
    private Integer per_page;
    private Integer total;
    private Integer total_pages;
    private List<ReqresUser> data;
    private ReqresSupportDTO support;
}
