package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.exception.ErrorDetails;
import com.werdersoft.personapi.exception.ErrorDetailsUtils;
import com.werdersoft.personapi.util.ClassFactoryUtils;
import com.werdersoft.personapi.util.DBQueriesUtils;
import com.werdersoft.personapi.util.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@DisplayName("Тесты rest-контроллера EmployeeController")
@Testcontainers
public class EmployeeControllerTest {

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

    @Autowired
    private FileUtils fileUtils;

    private final String API_PATH = "/api/v1/employees";

    @BeforeAll
    static void init() {
        container.start();
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
    @DisplayName("POST - Успешное cоздание Employee")
    public void createEmployeeSuccess() {

        // arrange
        UUID personId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        UUID subdivisionId = UUID.randomUUID();

        dbQueriesUtils.addRecordPersonWithId(personId);
        dbQueriesUtils.addRecordCompanyWithId(companyId);
        dbQueriesUtils.addRecordSubdivisionWithId(subdivisionId);
        dbQueriesUtils.addRecordSubdivisionCompanyWithIds(subdivisionId, companyId);

        EmployeeDTO requestEmployeeDTO = ClassFactoryUtils.newEmployeeDTO(personId, subdivisionId);
        EmployeeDTO expectedEmployeeDTOAnswer = ClassFactoryUtils.newEmployeeDTO(personId, subdivisionId);
        EmployeeDTO expectedEmployeeEntity = ClassFactoryUtils.newEmployeeDTO(personId, subdivisionId);

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .body(Mono.just(requestEmployeeDTO), EmployeeDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        UUID id = actualResponseDTO.getId();
        EmployeeDTO actualEmployeeEntity= dbQueriesUtils.selectEmployeeById(id).get(0);

        // assert
        expectedEmployeeDTOAnswer.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedEmployeeDTOAnswer);

        expectedEmployeeEntity.setId(id);
        assertThat(actualEmployeeEntity).isEqualTo(expectedEmployeeEntity);

    }

    @Test
    @DisplayName("POST - Создание Employee - пустые person и subdivision")
    public void createEmployeeFail() throws Exception {

        // arrange
        String JSONRequest = fileUtils.readFile("requests/create-employee-with-empty-person.json");

        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsUtils.newErrorDetails400Check(List.of(
                        "Field 'personId' cannot be empty",
                        "Field 'subdivisionId' cannot be empty"
                ));

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSONRequest)
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
    @DisplayName("GET - Успешное получение Employee по ID")
    public void getEmployeeByIdSuccess() {

        // arrange
        UUID personId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        UUID subdivisionId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        dbQueriesUtils.addRecordPersonWithId(personId);
        dbQueriesUtils.addRecordCompanyWithId(companyId);
        dbQueriesUtils.addRecordSubdivisionWithId(subdivisionId);
        dbQueriesUtils.addRecordSubdivisionCompanyWithIds(subdivisionId, companyId);
        dbQueriesUtils.addRecordEmployeeWithId(id, personId, subdivisionId);

        EmployeeDTO expectedEmployeeDTOAnswer = ClassFactoryUtils.newEmployeeDTO(personId, subdivisionId);
        expectedEmployeeDTOAnswer.setId(id);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedEmployeeDTOAnswer);

    }

    @Test
    @DisplayName("GET - Получение всех Employee")
    public void getAllEmployeesSuccess() {

        // arrange
        UUID personId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        UUID subdivisionId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        dbQueriesUtils.addRecordPersonWithId(personId);
        dbQueriesUtils.addRecordCompanyWithId(companyId);
        dbQueriesUtils.addRecordSubdivisionWithId(subdivisionId);
        dbQueriesUtils.addRecordSubdivisionCompanyWithIds(subdivisionId, companyId);
        dbQueriesUtils.addRecordEmployeeWithId(id, personId, subdivisionId);

        EmployeeDTO employeeDTO = ClassFactoryUtils.newEmployeeDTO(personId, subdivisionId);
        employeeDTO.setId(id);
        List<EmployeeDTO> expectedEmployeesListAnswer = List.of(employeeDTO);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedEmployeesListAnswer);

    }

}
