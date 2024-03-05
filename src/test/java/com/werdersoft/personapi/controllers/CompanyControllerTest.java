package com.werdersoft.personapi.controllers;

import com.werdersoft.personapi.BaseControllerTest;
import com.werdersoft.personapi.company.Company;
import com.werdersoft.personapi.company.CompanyDTO;
import com.werdersoft.personapi.exception.ErrorDetails;
import com.werdersoft.personapi.utils.ErrorDetailsFactory;
import com.werdersoft.personapi.utils.TestDataFactory;
import com.werdersoft.personapi.utils.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты rest-контроллера CompanyController")
public class CompanyControllerTest extends BaseControllerTest {

    @Autowired
    private FileUtils fileUtils;

    private static final String API_PATH = "/api/v1/companies";

    @Test
    @DisplayName("Cоздание Company - Успех")
    public void createCompanySuccess() {

        // arrange
        CompanyDTO requestCompanyDTO = TestDataFactory.newCompanyDTO();
        CompanyDTO expectedCompanyDTOAnswer = TestDataFactory.newCompanyDTO();
        Company expectedCompanyEntity = TestDataFactory.newCompany();

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
    @DisplayName("Cоздание Company - Некорректное имя")
    public void createCompanyFail_IncorrectName() {

        // arrange
        CompanyDTO requestCompanyDTO = TestDataFactory.newCompanyDTO();
        requestCompanyDTO.setName("");
        requestCompanyDTO.setRegion(null);
        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsFactory.newErrorDetails400Check(List.of(
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
    @DisplayName("Cоздание Company - Некорректный регион")
    public void createCompanyFail_IncorrectRegion() {

        // arrange
        String JSONRequest = fileUtils.readFile("json-requests/create-company-with-wrong-region.json");

        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsFactory.newErrorDetails400Check(List.of(
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
    @DisplayName("Получение всех Company")
    @Sql(CREATE_COMPANY_SQL_PATH)
    public void getAllCompaniesSuccess() {

        // arrange
        CompanyDTO companyDTO = TestDataFactory.newCompanyDTO();
        companyDTO.setId(TEST_COMPANY_ID);
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
