package com.werdersoft.personapi.reqres;

import lombok.Getter;

import java.util.List;

@Getter
public class ReqresListUsers {
    private Integer page;
    private Integer per_page;
    private Integer total;
    private Integer total_pages;
    private List<ReqresUser> data;
    private ReqresSupport support;
}
