package com.werdersoft.personapi.person;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//TODO: ReqresClient
@Component
@Slf4j
public class PersonDataLoading {

    //TODO: Вынести в application.properties
    final private String externalServiceUrl = "https://reqres.in/api/users";

    //TODO: getPersonByID
    //TODO: Нужно возвращать DTO АПИ
    public Person loadPersonByID(Integer externalId) {

        RestTemplate restTemplate = new RestTemplate(); //Заменить на WebClient и DI

        //TODO: Выполнение HTTP-запроса
        ResponseEntity<String> response =
                restTemplate.getForEntity(externalServiceUrl + "/" + externalId, String.class);
        //TODO: Написать DTO-класс для тела ответа внешнего API и ответ принимать в экземпляр этого класса
        //TODO: Object Mapper И JSON Node не требуются
        ObjectMapper objectMapper = new ObjectMapper();

        Person person = new Person();
        //TODO: Преобразование внешнего класса в Person маппингом

        //TODO: Обработка ответа
        //TODO: Написать аспект для обработки ошибок
        if (response.getStatusCode().is2xxSuccessful()) {

            String responseBody = response.getBody();

            try {
                // Преобразование JSON-строки в объект JsonNode
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                JsonNode data = jsonNode.get("data");

                person.setExternalID(data.get("id").asInt());
                person.setName(data.get("first_name").asText());
                person.setEmail(data.get("email").asText());

            } catch (Exception e) {
                log.error("Error when reading json: " + e.getMessage());
            }

        } else {
            log.error("Wrong response when loadPersonByID: " + response.getStatusCodeValue());
        }

        return person;

    }

    //TODO: getPersons
    public List<Person> loadAllPersons(List<Integer> existingIds) {

        RestTemplate restTemplate = new RestTemplate();

        // Выполнение HTTP-запроса
        ResponseEntity<String> response = restTemplate.getForEntity(externalServiceUrl + "?page=1", String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Person> persons = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();

            try {
                // Преобразование JSON-строки в объект JsonNode
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                JsonNode data = jsonNode.get("data");

                for (JsonNode node : data) {
                    Integer externalID = node.get("id").asInt();
                    if (existingIds.contains(externalID)) {
                        continue;
                    }
                    Person person = new Person();
                    person.setExternalID(node.get("id").asInt());
                    person.setName(node.get("first_name").asText());
                    person.setEmail(node.get("email").asText());
                    persons.add(person);
                }

            } catch (Exception e) {
                log.error("Error when reading json: " + e.getMessage());
            }

        } else {
            log.error("Wrong response when loadAllPersons: " + response.getStatusCodeValue());
        }

        return persons;
    }

}
