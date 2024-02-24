package com.werdersoft.personapi.person;

import com.werdersoft.personapi.exception.ErrorDetails;
import com.werdersoft.personapi.exception.ErrorDetailsUtils;
import com.werdersoft.personapi.util.ClassFactoryUtils;
import com.werdersoft.personapi.util.DBQueriesUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера PersonController")
@ActiveProfiles("test")
@Testcontainers
// TODO Создать application-test.properties c настройками datasource
public class PersonControllerTest {

    @Container
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DBQueriesUtils dbQueriesUtils;

    private final String API_PATH = "/api/v1/persons";

    @BeforeAll
    static void beforeAll() {
        container.start();
        String urlContainer = container.getJdbcUrl();
    }

    @BeforeEach
    void prepare() {
        dbQueriesUtils.deleteAllRecords();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    @DisplayName("POST - Успешное cоздание Person")
    public void createPersonSuccess() {

        // arrange
        PersonDTO requestPersonDTO = ClassFactoryUtils.newPersonDTO();
        PersonDTO expectedPersonDTOAnswer = ClassFactoryUtils.newPersonDTO();
        Person expectedPersonEntity = ClassFactoryUtils.newPerson();

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
    @DisplayName("POST - Провальное cоздание Person")
    public void createPersonFail() {

        // arrange
        PersonDTO requestPersonDTO = ClassFactoryUtils.newPersonDTO();
        requestPersonDTO.setAge(-30);
        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsUtils.newErrorDetails400Check(List.of("Age should be greater than 0"));

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
        // TODO Обработка ErrorDetails после слияния

    }

    @Test
    @DisplayName("GET - Успешное получение Person по ID")
    public void getPersonByIdSuccess() {

        // arrange
        UUID id = UUID.randomUUID();
        PersonDTO expectedPersonDTOAnswer = ClassFactoryUtils.newPersonDTO();
        expectedPersonDTOAnswer.setId(id);
        dbQueriesUtils.addRecordPersonWithId(id);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTOAnswer);

    }

    @Test
    @DisplayName("GET - Провальное получение Person по ID")
    public void getPersonByIdFail() {

        // arrange
        UUID failId = UUID.randomUUID();
        String uri = API_PATH + "/" + failId;
        ErrorDetails expectedErrorDetailsAnswer = ErrorDetailsUtils.newErrorDetails404IncorrectURI(uri);

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
    @DisplayName("GET - Получение всех Person")
    public void getAllPersonsSuccess() {

        // arrange
        UUID id = UUID.randomUUID();
        dbQueriesUtils.addRecordPersonWithId(id);

        PersonDTO personDTO = ClassFactoryUtils.newPersonDTO();
        personDTO.setId(id);
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
    @DisplayName("UPDATE - Обновление Person по ID")
    public void updatePersonByIdSuccess() {

        // arrange
        UUID id = UUID.randomUUID();

        PersonDTO requestPersonDTO = PersonDTO.builder().name("Tom").age(28).build();
        PersonDTO expectedPersonDTOAnswer = PersonDTO.builder().id(id).name("Tom").age(28).build();
        dbQueriesUtils.addRecordPersonWithId(id);
        Person expectedPersonEntity = Person.builder().id(id).name("Tom").age(28).build();

        //act
        var actualResponseDTO = webTestClient
                .put()
                .uri(API_PATH + "/" + id)
                .body(Mono.just(requestPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();
        Person actualPersonEntity = dbQueriesUtils.selectPersonsById(id).get(0);

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTOAnswer);
        assertThat(actualPersonEntity).isEqualTo(expectedPersonEntity);

    }

    @Test
    @DisplayName("DELETE - Удаление Person по ID")
    public void deletePersonByIdSuccess() {

        // arrange
        UUID id = UUID.randomUUID();
        dbQueriesUtils.addRecordPersonWithId(id);

        //act
        var actualResponseDTO = webTestClient
                .delete()
                .uri(API_PATH + "/" + id)
                .exchange()
                .expectStatus().isNoContent();
        List<Person> actualPersonEntity = dbQueriesUtils.selectPersonsById(id);

        // assert
        assertThat(actualPersonEntity).isEmpty();

    }

}