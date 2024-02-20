package com.werdersoft.personapi.person;

import com.werdersoft.personapi.exception.ErrorDetails;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера PersonController")
@Testcontainers
public class PersonControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String apiPath;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void prepare() {
        apiPath = "/api/v1/persons";
    }

    @Test
    @DisplayName("POST - Успешное cоздание Person")
    public void createPersonSuccess() {

        // arrange
        PersonDTO expectedPersonDTO = getTestPersonDTO();
        Person expectedPerson = getTestPerson();
        PersonDTO requestPersonDTO = getTestPersonDTO();

        // act & assert
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(requestPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponseDTO).isNotNull();
        UUID id = actualResponseDTO.getId();

        Person actualPerson = getPersonsById(id).get(0);

        expectedPersonDTO.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTO);

        expectedPerson.setId(id);
        assertThat(actualPerson).isEqualTo(expectedPerson);

    }

    @Test
    @DisplayName("POST - Провальное cоздание Person")
    public void createPersonFail() {

        // arrange
        PersonDTO requestPersonDTO = getTestPersonDTO();
        requestPersonDTO.setAge(-30);

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(requestPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetails.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isNotNull();
        assertThat(actualResponseDTO.getStatus()).isEqualTo(400);
        assertThat(actualResponseDTO.getErrors()).isNotEmpty();
        //TODO Обработка ErrorDetails после слияния

    }

    @Test
    @DisplayName("GET - Успешное получение Person по ID")
    public void getPersonByIdSuccess() throws Exception {

        // arrange
        UUID id = UUID.randomUUID();
        addRecordByEntity(id);

        PersonDTO expectedPersonDTO = getTestPersonDTO();
        expectedPersonDTO.setId(id);

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
        deleteAllRecords();
        UUID id = UUID.randomUUID();
        addRecordByEntity(id);

        List<PersonDTO> expectedPersonsList = new ArrayList<>();
        PersonDTO personDTO = getTestPersonDTO();
        personDTO.setId(id);
        expectedPersonsList.add(personDTO);

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
        assertThat(actualResponseDTO).isEqualTo(expectedPersonsList);

    }

    @Test
    @DisplayName("UPDATE - Обновление Person по ID")
    public void updatePersonByIdSuccess() throws Exception {

        // arrange
        UUID id = UUID.randomUUID();
        addRecordByEntity(id);
        Person expectedPerson = new Person();
        expectedPerson.setId(id);
        expectedPerson.setName("Tom");
        expectedPerson.setAge(28);

        PersonDTO expectedPersonDTO = getTestPersonDTO();
        expectedPersonDTO.setId(id);
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
        Person actualPerson = getPersonsById(id).get(0);

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTO);
        assertThat(actualPerson).isEqualTo(expectedPerson);

    }

    @Test
    @DisplayName("DELETE - Удаление Person по ID")
    public void deletePersonByIdSuccess() throws Exception {

        // arrange
        UUID id = UUID.randomUUID();
        addRecordByEntity(id);

        //act & assert
        var actualResponseDTO = webTestClient
                .delete()
                .uri(apiPath+ "/" + id)
                .exchange()
                .expectStatus().isNoContent();
        List<Person> actualPerson = getPersonsById(id);

        // assert
        assertThat(actualPerson).isEmpty();

    }

    private PersonDTO getTestPersonDTO() {
        PersonDTO testPersonDTO = new PersonDTO();
        testPersonDTO.setName("Sam");
        testPersonDTO.setAge(22);
        return testPersonDTO;
    }

    private Person getTestPerson() {
        Person testPerson = new Person();
        testPerson.setName("Sam");
        testPerson.setAge(22);
        return testPerson;
    }

    private void addRecordByEntity(UUID id) {
        String sql = "INSERT INTO person(id, name, age) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "Sam");
            preparedStatement.setInt(3, 22);
        });
    }

    private void deleteAllRecords() {
        String sql = "DELETE from person";
        jdbcTemplate.execute(sql);
    }

    private List<Person> getPersonsById(UUID id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, ps -> ps.setObject(1, id), (resultSet, i) -> {
                Person person = new Person();
                person.setId(resultSet.getObject(1, UUID.class));
                person.setName(resultSet.getString(2));
                person.setAge(resultSet.getInt(3));
                return person;
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

}
