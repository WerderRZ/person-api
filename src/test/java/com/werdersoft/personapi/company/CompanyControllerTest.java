package com.werdersoft.personapi.company;

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
@DisplayName("Тесты rest-контроллера CompanyController")
@Testcontainers
public class CompanyControllerTest {

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

    private final String API_PATH = "/api/v1/companies";

    @BeforeAll
    static void beforeAll() {
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
    @DisplayName("POST - Успешное cоздание Company")
    public void createCompanySuccess() {

        // arrange
        CompanyDTO requestCompanyDTO = ClassFactoryUtils.newCompanyDTO();
        CompanyDTO expectedCompanyDTOAnswer = ClassFactoryUtils.newCompanyDTO();
        Company expectedCompanyEntity = ClassFactoryUtils.newCompany();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .body(Mono.just(requestCompanyDTO), CompanyDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CompanyDTO.class)
                .returnResult()
                .getResponseBody();

        UUID id = actualResponseDTO.getId();
        Company actualCompanyEntity = dbQueriesUtils.selectCompaniesById(id).get(0);

        // assert
        expectedCompanyDTOAnswer.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedCompanyDTOAnswer);

        expectedCompanyEntity.setId(id);
        assertThat(actualCompanyEntity).isEqualTo(expectedCompanyEntity);

    }

    @Test
    @DisplayName("POST - Cоздание Company - некорректное имя")
    public void createCompanyFail_IncorrectName() {

        // arrange
        CompanyDTO requestCompanyDTO = ClassFactoryUtils.newCompanyDTO();
        requestCompanyDTO.setName("");
        requestCompanyDTO.setRegion(null);
        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsUtils.newErrorDetails400Check(List.of(
                        "Name should have at least 1 character",
                        "Name should not be empty",
                        "Region should not be empty"
                ));

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .body(Mono.just(requestCompanyDTO), CompanyDTO.class)
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
    @DisplayName("POST - Cоздание Company - некорректный регион")
    public void createCompanyFail_IncorrectRegion() throws Exception {

        // arrange
        String JSONRequest = fileUtils.readFile("requests/create-company-with-wrong-region.json");

        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsUtils.newErrorDetails400Check(List.of(
                        "Сouldn't read the message:" + API_PATH
                ));

        // act  & assert
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
    @DisplayName("GET - Получение всех Company")
    public void getAllCompaniesSuccess() {

        // arrange
        UUID id = UUID.randomUUID();
        dbQueriesUtils.addRecordCompanyWithId(id);

        CompanyDTO companyDTO = ClassFactoryUtils.newCompanyDTO();
        companyDTO.setId(id);
        List<CompanyDTO> expectedCompaniesListAnswer = List.of(companyDTO);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedCompaniesListAnswer);

    }

}
