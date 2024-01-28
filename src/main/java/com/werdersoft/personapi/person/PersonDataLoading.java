package com.werdersoft.personapi.person;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PersonDataLoading {

    final private String externalServiceUrl = "https://reqres.in/api/users";

    public Person loadPersonByID(Integer externalId) {

        RestTemplate restTemplate = new RestTemplate();

        // Выполнение HTTP-запроса
        ResponseEntity<String> response =
                restTemplate.getForEntity(externalServiceUrl + "/" + externalId, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        Person person = new Person();

        // Обработка ответа
        if (response.getStatusCode().is2xxSuccessful()) {

            String responseBody = response.getBody();
            System.out.println("Ответ от сервера: " + responseBody);

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

    public List<Person> loadAllPersons(List<Integer> existingIds) {

        RestTemplate restTemplate = new RestTemplate();

        // Выполнение HTTP-запроса
        ResponseEntity<String> response = restTemplate.getForEntity(externalServiceUrl + "?page=1", String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Person> persons = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            System.out.println("Ответ от сервера: " + responseBody);

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
