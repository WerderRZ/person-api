package com.werdersoft.personapi.subdivision;

import com.werdersoft.personapi.company.CompanyDTO;
import com.werdersoft.personapi.company.CompanyService;
import com.werdersoft.personapi.enums.Region;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера SubdivisionController")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubdivisionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SubdivisionService subdivisionService;

    private String apiPath;

    private CompanyDTO testCompanyDTO;

    @BeforeAll
    void init() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Werdersoft");
        companyDTO.setRegion(Region.ASIA);
        testCompanyDTO = companyService.createCompany(companyDTO);
    }

    @BeforeEach
    void prepare() {
        apiPath = "/api/v1/subdivisions";
    }

    @Test
    @DisplayName("POST - Успешное cоздание Subdivision")
    public void createSubdivisionSuccess() throws Exception {

        // arrange
        SubdivisionDTO actualSubdivisionDTO = getTestSubdivisionDTO();
        UUID companyId = testCompanyDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualSubdivisionDTO), SubdivisionDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isNotNull();
        assertThat(actualResponseDTO.getName()).isNotBlank().isEqualTo(actualSubdivisionDTO.getName());
        assertThat(actualResponseDTO.getCompaniesIds()).contains(companyId);

    }

    @Test
    @DisplayName("GET - Успешное получение Subdivision по ID")
    public void getPersonByIdSuccess() throws Exception {

        // arrange
        SubdivisionDTO expectedSubdivisionDTO = subdivisionService.createSubdivision(getTestSubdivisionDTO());
        UUID id = expectedSubdivisionDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedSubdivisionDTO);

    }

    @Test
    @DisplayName("GET - Получение всех Subdivision")
    public void getAllSubdivisionsSuccess() throws Exception {

        // arrange
        SubdivisionDTO expectedSubdivisionDTO = subdivisionService.createSubdivision(getTestSubdivisionDTO());
        UUID id = expectedSubdivisionDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).contains(expectedSubdivisionDTO);

    }

    private SubdivisionDTO getTestSubdivisionDTO() {
        List<UUID> companiesIds = new ArrayList<>();
        companiesIds.add(testCompanyDTO.getId());

        SubdivisionDTO testSubdivisionDTO = new SubdivisionDTO();
        testSubdivisionDTO.setName("IT");
        testSubdivisionDTO.setCompaniesIds(companiesIds);
        return testSubdivisionDTO;
    }

}
