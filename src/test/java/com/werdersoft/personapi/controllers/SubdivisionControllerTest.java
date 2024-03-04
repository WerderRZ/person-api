package com.werdersoft.personapi.controllers;

import com.werdersoft.personapi.BaseControllerTest;
import com.werdersoft.personapi.subdivision.Subdivision;
import com.werdersoft.personapi.subdivision.SubdivisionDTO;
import com.werdersoft.personapi.utils.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.test.context.jdbc.Sql;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты rest-контроллера SubdivisionController")
public class SubdivisionControllerTest extends BaseControllerTest {

    private final String API_PATH = "/api/v1/subdivisions";

    @Test
    @DisplayName("Создание Subdivision - Успех")
    @Sql(CREATE_COMPANY_SQL_PATH)
    public void createSubdivisionSuccess() {

        // arrange
        SubdivisionDTO requestSubdivisionDTO = TestDataFactory.newSubdivisionDTO(TEST_COMPANY_ID);
        requestSubdivisionDTO.setEmployeesIds(null);
        SubdivisionDTO expectedSubdivisionDTOAnswer = TestDataFactory.newSubdivisionDTO(TEST_COMPANY_ID);
        Subdivision expectedSubdivisionEntity = TestDataFactory.newSubdivision();

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
        Map<UUID, UUID> expectedSubdivisionCompanyMap = TestDataFactory.newSubdivisionCompanyMap(TEST_COMPANY_ID, id);
        Map<UUID, UUID> actualSubdivisionsCompaniesMap =
                dbQueriesUtils.selectSubdivisionsCompaniesRowsById(id, TEST_COMPANY_ID).get(0);

        // assert
        expectedSubdivisionDTOAnswer.setId(id);
        assertThat(actualResponseDTO).isEqualTo(expectedSubdivisionDTOAnswer);

        expectedSubdivisionEntity.setId(id);
        assertThat(actualSubdivisionEntity).isEqualTo(expectedSubdivisionEntity);

        assertThat(actualSubdivisionsCompaniesMap).isEqualTo(expectedSubdivisionCompanyMap);

    }

    @Test
    @DisplayName("Получение Subdivision по ID - Успех")
    @Sql({CREATE_COMPANY_SQL_PATH, CREATE_SUBDIVISION_SQL_PATH, CREATE_SUBDIVISIONS_COMPANIES_SQL_PATH})
    public void getSubdivisionByIdSuccess() {

        // arrange
        SubdivisionDTO expectedSubdivisionDTOAnswer = TestDataFactory.newSubdivisionDTO(TEST_COMPANY_ID);
        expectedSubdivisionDTOAnswer.setId(TEST_SUBDIVISION_ID);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/" + TEST_SUBDIVISION_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubdivisionDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedSubdivisionDTOAnswer);

    }

    @Test
    @DisplayName("Получение всех Subdivision")
    @Sql({CREATE_COMPANY_SQL_PATH, CREATE_SUBDIVISION_SQL_PATH, CREATE_SUBDIVISIONS_COMPANIES_SQL_PATH})
    public void getAllSubdivisionsSuccess() {

        // arrange
        SubdivisionDTO subdivisionDTO = TestDataFactory.newSubdivisionDTO(TEST_COMPANY_ID);
        subdivisionDTO.setId(TEST_SUBDIVISION_ID);
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
