package com.werdersoft.personapi.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера PersonController")
public class PersonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PersonService personService;

    private String apiPath;

    @BeforeEach
    void prepare() {
        apiPath = "/api/v1/persons";
    }

    @Test
    @DisplayName("POST - Успешное cоздание Person")
    public void createPersonSuccess() throws Exception {

        // arrange
        PersonDTO actualPersonDTO = getTestPerson();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isNotNull();
        assertThat(actualResponseDTO.getName()).isNotBlank().isEqualTo(actualPersonDTO.getName());

        assertThat(actualResponseDTO.getAge()).isEqualTo(actualPersonDTO.getAge());

    }

    @Test
    @DisplayName("POST - Провальное cоздание Person")
    public void createPersonFail() throws Exception {

        // arrange
        PersonDTO actualPersonDTO = getTestPerson();
        actualPersonDTO.setAge(-30);

        // act  & assert
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    @DisplayName("GET - Успешное получение Person по ID")
    public void getPersonByIdSuccess() throws Exception {

        // arrange
        PersonDTO expectedPersonDTO = personService.createPerson(getTestPerson());
        UUID id = expectedPersonDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTO);

    }

    @Test
    @DisplayName("GET - Провальное получение Person по ID")
    public void getPersonByIdFail() throws Exception {

        // arrange
        UUID failId = UUID.randomUUID();

        // act & assert
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath + "/" + failId)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    @DisplayName("GET - Получение всех Person")
    public void getAllPersonsSuccess() throws Exception {

        // arrange
        PersonDTO expectedPersonDTO = personService.createPerson(getTestPerson());
        UUID id = expectedPersonDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).contains(expectedPersonDTO);

    }

    @Test
    @DisplayName("UPDATE - Обновление Person по ID")
    public void updatePersonByIdSuccess() throws Exception {

        // arrange
        PersonDTO expectedPersonDTO = personService.createPerson(getTestPerson());
        UUID id = expectedPersonDTO.getId();
        expectedPersonDTO.setName("Tom");
        expectedPersonDTO.setAge(28);

        //act
        var actualResponseDTO = webTestClient
                .put()
                .uri(apiPath+ "/" + id)
                .body(Mono.just(expectedPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTO);

    }

    @Test
    @DisplayName("DELETE - Удаление Person по ID")
    public void deletePersonByIdSuccess() throws Exception {

        // arrange
        PersonDTO expectedPersonDTO = personService.createPerson(getTestPerson());
        UUID id = expectedPersonDTO.getId();

        //act & assert
        var actualResponseDTO = webTestClient
                .delete()
                .uri(apiPath+ "/" + id)
                .exchange()
                .expectStatus().isNoContent();

    }

    private PersonDTO getTestPerson() {
        PersonDTO testPersonDTO = new PersonDTO();
        testPersonDTO.setName("Sam");
        testPersonDTO.setAge(22);
        return testPersonDTO;
    }

}
