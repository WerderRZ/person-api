package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.util.ClassFactoryUtils;
import com.werdersoft.personapi.util.DBQueriesUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера SubdivisionController")
@Testcontainers
public class SubdivisionControllerTest {

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

    private final String API_PATH = "/api/v1/subdivisions";

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
    @DisplayName("POST - Успешное cоздание Subdivision")
    public void createSubdivisionSuccess() {

        // arrange
        UUID companyId = UUID.randomUUID();
        dbQueriesUtils.addRecordCompanyWithId(companyId);

        SubdivisionDTO requestSubdivisionDTO = ClassFactoryUtils.newSubdivisionDTO(companyId);
        requestSubdivisionDTO.setEmployeesIds(null);
        SubdivisionDTO expectedSubdivisionDTOAnswer = ClassFactoryUtils.newSubdivisionDTO(companyId);
        Subdivision expectedSubdivisionEntity = ClassFactoryUtils.newSubdivision();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(API_PATH)
                .body(Mono.just(requestSubdivisionDTO), SubdivisionDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        UUID id = actualResponseDTO.getId();
        Subdivision actualSubdivisionEntity= dbQueriesUtils.selectSubdivisionById(id).get(0);
        Map<UUID, UUID> expectedSubdivisionCompanyMap = ClassFactoryUtils.newSubdivisionCompanyMap(companyId, id);
        Map<UUID, UUID> actualSubdivisionsCompaniesMap =
                dbQueriesUtils.selectSubdivisionsCompaniesRowsById(id, companyId).get(0);

        // assert
        expectedSubdivisionDTOAnswer.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedSubdivisionDTOAnswer);

        expectedSubdivisionEntity.setId(id);
        assertThat(actualSubdivisionEntity).isEqualTo(expectedSubdivisionEntity);

        assertThat(actualSubdivisionsCompaniesMap).isEqualTo(expectedSubdivisionCompanyMap);

    }

    @Test
    @DisplayName("GET - Успешное получение Subdivision по ID")
    public void getSubdivisionByIdSuccess() {

        // arrange
        UUID companyId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        dbQueriesUtils.addRecordCompanyWithId(companyId);
        dbQueriesUtils.addRecordSubdivisionWithId(id);
        dbQueriesUtils.addRecordSubdivisionCompanyWithIds(id, companyId);

        SubdivisionDTO expectedSubdivisionDTOAnswer = ClassFactoryUtils.newSubdivisionDTO(companyId);
        expectedSubdivisionDTOAnswer.setId(id);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedSubdivisionDTOAnswer);

    }

    @Test
    @DisplayName("GET - Получение всех Subdivision")
    public void getAllSubdivisionsSuccess() {

        // arrange
        UUID companyId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        dbQueriesUtils.addRecordCompanyWithId(companyId);
        dbQueriesUtils.addRecordSubdivisionWithId(id);
        dbQueriesUtils.addRecordSubdivisionCompanyWithIds(id, companyId);

        SubdivisionDTO subdivisionDTO = ClassFactoryUtils.newSubdivisionDTO(companyId);
        subdivisionDTO.setId(id);
        List<SubdivisionDTO> expectedSubdivisionsListAnswer = List.of(subdivisionDTO);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedSubdivisionsListAnswer);

    }

}
