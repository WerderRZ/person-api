package com.werdersoft.personapi.reqres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class ReqresService {

    private final  WebClient.Builder builder = WebClient.builder();

    @Value("${reqres.url-get-by-id}")
    private String urlGetById;

    @Value("${reqres.url-get-list}")
    private String urlGetList;

    public ReqresUser getPersonById(Integer externalId) {

        ReqresSingleUser reqresSingleUser = builder.build()
                .get()
                .uri(urlGetById + externalId)
                .retrieve()
                .bodyToMono(ReqresSingleUser.class)
//                .doOnError(error -> new getError(error.))
                .block();
        //TODO: Как обработать ситуацию, когда reqresSingleUser может быть null
        return reqresSingleUser.getData();

    }

    public List<ReqresUser> getAllPersons() {

        ReqresListUsers reqresListUsers = builder.build()
                .get()
                .uri(urlGetList)
                .retrieve()
                .bodyToMono(ReqresListUsers.class)
                .block();

        return reqresListUsers.getData();

    }

}
