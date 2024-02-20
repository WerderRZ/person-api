package com.werdersoft.personapi.company;

import com.werdersoft.personapi.enums.Region;
import com.werdersoft.personapi.exception.ErrorDetails;
import com.werdersoft.personapi.initializer.Postgres;
import com.werdersoft.personapi.util.JsonFileReaderService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера CompanyController")
@ContextConfiguration(initializers = {Postgres.Initializer.class})
public class CompanyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JsonFileReaderService jsonFileReaderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String apiPath;

    @BeforeAll
    static void beforeAll() {
        Postgres.container.start();
    }

    @AfterAll
    static void afterAll() {
        Postgres.container.stop();
    }

    @BeforeEach
    void prepare() {
        apiPath = "/api/v1/companies";
    }

    @Test
    @DisplayName("POST - Успешное cоздание Company")
    public void createCompanySuccess() throws Exception {

        // arrange
        CompanyDTO expectedCompanyDTO = getTestCompanyDTO();
        Company expectedCompany = getTestCompany();
        CompanyDTO requestCompanyDTO = getTestCompanyDTO();

        // act & assert
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(requestCompanyDTO), CompanyDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CompanyDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponseDTO).isNotNull();
        UUID id = actualResponseDTO.getId();

        Company actualCompany = getCompaniesById(id).get(0);

        expectedCompanyDTO.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedCompanyDTO);

        expectedCompany.setId(id);
        assertThat(actualCompany).isEqualTo(expectedCompany);

    }

    @Test
    @DisplayName("POST - Cоздание Company - некорректное имя")
    public void createCompanyFail_IncorrectName() throws Exception {

        // arrange
        CompanyDTO actualCompanyDTO = getTestCompanyDTO();
        actualCompanyDTO.setName("");

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualCompanyDTO), CompanyDTO.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetails.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isNotNull();
        assertThat(actualResponseDTO.getStatus()).isEqualTo(400);
        assertThat(actualResponseDTO.getErrors()).isNotEmpty();

    }

    @Test
    @DisplayName("POST - Cоздание Company - некорректный регион")
    public void createCompanyFail_IncorrectRegion() throws Exception {

        // arrange
        String JSONRequest =
                jsonFileReaderService.readJsonFile("requests/create-company-with-wrong-region.json");

        // act  & assert
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSONRequest)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    @DisplayName("GET - Получение всех Company")
    public void getAllPersonsSuccess() throws Exception {

        // arrange
        deleteAllRecords();
        UUID id = UUID.randomUUID();
        addRecordByEntity(id);

        List<CompanyDTO> expectedCompaniesList = new ArrayList<>();
        CompanyDTO companyDTO = getTestCompanyDTO();
        companyDTO.setId(id);
        expectedCompaniesList.add(companyDTO);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CompanyDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedCompaniesList);

    }

    private CompanyDTO getTestCompanyDTO() {
        CompanyDTO testCompanyDTO = new CompanyDTO();
        testCompanyDTO.setName("Werdersoft");
        testCompanyDTO.setRegion(Region.ASIA);
        testCompanyDTO.setSubdivisionsIds(new ArrayList<>());
        return testCompanyDTO;
    }

    private Company getTestCompany() {
        Company testCompany = new Company();
        testCompany.setName("Werdersoft");
        testCompany.setRegion(Region.ASIA);
        return testCompany;
    }

    private List<Company> getCompaniesById(UUID id) {
        String sql = "SELECT * FROM company WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, ps -> ps.setObject(1, id), (resultSet, i) -> {
                Company company = new Company();
                company.setId(resultSet.getObject(1, UUID.class));
                company.setName(resultSet.getString(2));
                company.setRegion(Region.valueOf(resultSet.getString(3)));
                return company;
            });
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteAllRecords() {
        String sql = "DELETE from company";
        jdbcTemplate.execute(sql);
    }

    private void addRecordByEntity(UUID id) {
        String sql = "INSERT INTO company(id, name, region) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setObject(1, id);
            preparedStatement.setString(2, "Werdersoft");
            preparedStatement.setString(3, "ASIA");
        });
    }

}
