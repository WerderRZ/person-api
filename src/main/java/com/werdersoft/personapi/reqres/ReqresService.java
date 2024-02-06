package com.werdersoft.personapi.reqres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReqresService {

    private final WebClient webClient;

    @Value("${reqres.url-get-by-id}")
    private String URI_GET_BY_ID;

    @Value("${reqres.url-get-list}")
    private String URI_GET_LIST;

    public ReqresUser getPersonById(Integer externalId) {

        ReqresSingleUserDTO reqresSingleUserDTO = webClient
                .get()
                .uri(URI_GET_BY_ID + externalId)
                .retrieve()
                .bodyToMono(ReqresSingleUserDTO.class)
                .block();

        return reqresSingleUserDTO.getData();

    }

    public List<ReqresUser> getAllPersons() {

        ReqresUsersDTO reqresUsersDTO = webClient
                .get()
                .uri(URI_GET_LIST)
                .retrieve()
                .bodyToMono(ReqresUsersDTO.class)
                .block();

        return reqresUsersDTO.getData();

    }

}
