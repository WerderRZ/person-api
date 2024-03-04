package com.werdersoft.personapi.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.werdersoft.personapi.BaseControllerTest;
import com.werdersoft.personapi.exception.ErrorDetails;
import com.werdersoft.personapi.person.Person;
import com.werdersoft.personapi.person.PersonDTO;
import com.werdersoft.personapi.utils.ErrorDetailsFactory;
import com.werdersoft.personapi.utils.TestDataFactory;
import com.werdersoft.personapi.utils.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты rest-контроллера PersonController")
@ActiveProfiles("test")
public class PersonControllerTest extends BaseControllerTest {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private WireMockServer wireMockServer;

    private final String API_PATH = "/api/v1/persons";

    @Test
    @DisplayName("Создание Person - Успеx")
    public void createPersonSuccess() {

        // arrange
        PersonDTO requestPersonDTO = TestDataFactory.newPersonDTO();
        PersonDTO expectedPersonDTOAnswer = TestDataFactory.newPersonDTO();
        Person expectedPersonEntity = TestDataFactory.newPerson();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .body(Mono.just(requestPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        UUID id = actualResponseDTO.getId();
        Person actualPersonEntity = dbQueriesUtils.selectPersonsById(id).get(0);

        // assert
        expectedPersonDTOAnswer.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTOAnswer);

        expectedPersonEntity.setId(id);
        assertThat(actualPersonEntity).isEqualTo(expectedPersonEntity);

    }

    @Test
    @DisplayName("Cоздание Person - Провал")
    public void createPersonFail() {

        // arrange
        PersonDTO requestPersonDTO = TestDataFactory.newPersonDTO();
        requestPersonDTO.setAge(-30);
        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsFactory.newErrorDetails400Check(List.of("Age should be greater than 0"));

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .body(Mono.just(requestPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetails.class)
                .returnResult()
                .getResponseBody();

        // assert
        actualResponseDTO.setTimestamp(null);
        assertThat(actualResponseDTO).isEqualTo(expectedErrorDetailsAnswer);

    }

    @Test
    @DisplayName("Получение Person по ID - Успех")
    @Sql(CREATE_PERSON_SQL_PATH)
    public void getPersonByIdSuccess() {

        // arrange
        PersonDTO expectedPersonDTOAnswer = TestDataFactory.newPersonDTO();
        expectedPersonDTOAnswer.setId(TEST_PERSON_ID);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/" + TEST_PERSON_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTOAnswer);

    }

    @Test
    @DisplayName("Получение Person по ID - Провал")
    public void getPersonByIdFail() {

        // arrange
        UUID failId = UUID.randomUUID();
        String uri = API_PATH + "/" + failId;
        ErrorDetails expectedErrorDetailsAnswer = ErrorDetailsFactory.newErrorDetails404IncorrectURI(uri);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetails.class)
                .returnResult()
                .getResponseBody();

        // assert
        actualResponseDTO.setTimestamp(null);
        assertThat(actualResponseDTO).isEqualTo(expectedErrorDetailsAnswer);

    }

    @Test
    @DisplayName("Получение всех Person")
    @Sql(CREATE_PERSON_SQL_PATH)
    public void getAllPersonsSuccess() {

        // arrange
        PersonDTO personDTO = TestDataFactory.newPersonDTO();
        personDTO.setId(TEST_PERSON_ID);
        List<PersonDTO> expectedPersonsListAnswer = List.of(personDTO);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonsListAnswer);

    }

    @Test
    @DisplayName("Обновление Person по ID")
    @Sql(CREATE_PERSON_SQL_PATH)
    public void updatePersonByIdSuccess() {

        // arrange
        PersonDTO requestPersonDTO = PersonDTO.builder().name("Tom").age(28).build();
        PersonDTO expectedPersonDTOAnswer = PersonDTO.builder().id(TEST_PERSON_ID).name("Tom").age(28).build();
        Person expectedPersonEntity = Person.builder().id(TEST_PERSON_ID).name("Tom").age(28).build();

        //act
        var actualResponseDTO = webTestClient
                .put()
                .uri(API_PATH + "/" + TEST_PERSON_ID)
                .body(Mono.just(requestPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();
        Person actualPersonEntity = dbQueriesUtils.selectPersonsById(TEST_PERSON_ID).get(0);

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTOAnswer);
        assertThat(actualPersonEntity).isEqualTo(expectedPersonEntity);

    }

    @Test
    @DisplayName("Удаление Person по ID")
    @Sql(CREATE_PERSON_SQL_PATH)
    public void deletePersonByIdSuccess() {

        // arrange
        // act
        var actualResponseDTO = webTestClient
                .delete()
                .uri(API_PATH + "/" + TEST_PERSON_ID)
                .exchange()
                .expectStatus().isNoContent();
        List<Person> actualPersonEntity = dbQueriesUtils.selectPersonsById(TEST_PERSON_ID);

        // assert
        assertThat(actualPersonEntity).isEmpty();

    }

    @Test
    @DisplayName("Загрузка Person из внешней системы - Успех")
    public void loadPersonFromExternalSystemSuccess() {

        // arrange
        int externalId = 1;
        String jsonBody = fileUtils.readFile("json-requests/get-single-reqres-user.json");
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/users/" + externalId)).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonBody)));

        PersonDTO expectedPersonDTOAnswer = TestDataFactory.newPersonDTOExt(externalId);
        Person expectedPersonEntity = TestDataFactory.newPersonExt(externalId);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/load/" + externalId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        UUID id = actualResponseDTO.getId();
        Person actualPersonEntity = dbQueriesUtils.selectPersonsById(id).get(0);

        // assert
        expectedPersonDTOAnswer.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTOAnswer);

        expectedPersonEntity.setId(id);
        assertThat(actualPersonEntity).isEqualTo(expectedPersonEntity);

    }

    @Test
    @DisplayName("Загрузка Person из внешней системы - Провал")
    public void loadPersonFromExternalSystemFail_WrongExternalId() {

        // arrange
        int externalId = 456;
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/users/" + externalId)).willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("{}")));

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/load/" + externalId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetails.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO.getStatus()).isEqualTo(404);
        assertThat(actualResponseDTO.getErrors().get(0)).contains("Web client exception:");

    }

    @Test
    @DisplayName("Загрузка списка Persons из внешней системы - Успех")
    public void loadPersonsFromExternalSystemSuccess() {

        // arrange
        String jsonBody = fileUtils.readFile("json-requests/get-list-reqres-users.json");
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/users?page=1")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonBody)));

        int externalId = 1;
        PersonDTO personDTO = TestDataFactory.newPersonDTOExt(externalId);
        List<PersonDTO> expectedPersonsListAnswer = List.of(personDTO);
        Person expectedPersonEntity = TestDataFactory.newPersonExt(externalId);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/load")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        UUID id = actualResponseDTO.get(0).getId();
        Person actualPersonEntity = dbQueriesUtils.selectPersonsById(id).get(0);

        // assert
        actualResponseDTO.forEach(persDTO -> persDTO.setId(null));
        assertThat(actualResponseDTO).isEqualTo(expectedPersonsListAnswer);

        expectedPersonEntity.setId(id);
        assertThat(actualPersonEntity).isEqualTo(expectedPersonEntity);

    }

}