package com.werdersoft.personapi.company;

import com.werdersoft.personapi.enums.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера CompanyController")
public class CompanyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CompanyService companyService;

    private String apiPath;

    @BeforeEach
    void prepare() {
        apiPath = "/api/v1/companies";
    }

    @Test
    @DisplayName("POST - Успешное cоздание Company")
    public void createCompanySuccess() throws Exception {

        // arrange
        CompanyDTO actualCompanyDTO = getTestCompanyDTO();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualCompanyDTO), CompanyDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CompanyDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isNotNull();
        assertThat(actualResponseDTO.getName()).isNotBlank().isEqualTo(actualCompanyDTO.getName());
        assertThat(actualResponseDTO.getRegion()).isEqualTo(actualCompanyDTO.getRegion());

    }

    @Test
    @DisplayName("POST - Cоздание Company - некорректное имя")
    public void createCompanyFail_IncorrectName() throws Exception {

        // arrange
        CompanyDTO actualCompanyDTO = getTestCompanyDTO();
        actualCompanyDTO.setName("");

        // act  & assert
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualCompanyDTO), CompanyDTO.class)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    @DisplayName("POST - Cоздание Company - некорректный регион")
    public void createCompanyFail_IncorrectRegion() throws Exception {

        // arrange
        String JSONRequest = "{\"name\": \"Sam\",\"region\": \"Canada\"}";

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
        CompanyDTO expectedCompanyDTO = companyService.createCompany(getTestCompanyDTO());
        UUID id = expectedCompanyDTO.getId();

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
        assertThat(actualResponseDTO).contains(expectedCompanyDTO);

    }

    private CompanyDTO getTestCompanyDTO() {
        CompanyDTO testCompanyDTO = new CompanyDTO();
        testCompanyDTO.setName("Werdersoft");
        testCompanyDTO.setRegion(Region.ASIA);
        return testCompanyDTO;
    }

}
