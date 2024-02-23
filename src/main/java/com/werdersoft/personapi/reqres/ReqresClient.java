package com.werdersoft.personapi.reqres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReqresClient {

    private final WebClient webClient;

    @Value("${reqres.base-url}")
    private String baseURL;

    public ReqresUser getPersonById(Integer externalId) {

        ReqresSingleUserDTO reqresSingleUserDTO = webClient
                .get()
                .uri(String.format("%s/users/%s", baseURL, externalId))
                .retrieve()
                .bodyToMono(ReqresSingleUserDTO.class)
                .block();

        return reqresSingleUserDTO.getData();

    }

    public List<ReqresUser> getAllPersons() {

        ReqresUsersDTO reqresUsersDTO = webClient
                .get()
                .uri(String.format("%s/users?page=1", baseURL))
                .retrieve()
                .bodyToMono(ReqresUsersDTO.class)
                .block();

        return reqresUsersDTO.getData();

    }

}
