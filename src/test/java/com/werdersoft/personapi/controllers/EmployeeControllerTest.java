package com.werdersoft.personapi.controllers;

import com.werdersoft.personapi.BaseControllerTest;
import com.werdersoft.personapi.employee.EmployeeDTO;
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

@DisplayName("Тесты rest-контроллера EmployeeController")
public class EmployeeControllerTest extends BaseControllerTest {

    @Autowired
    private FileUtils fileUtils;

    private final String API_PATH = "/api/v1/employees";

    @Test
    @DisplayName("Создание Employee - Успех")
    @Sql({CREATE_PERSON_SQL_PATH, CREATE_COMPANY_SQL_PATH, CREATE_SUBDIVISION_SQL_PATH,
            CREATE_SUBDIVISIONS_COMPANIES_SQL_PATH})
    public void createEmployeeSuccess() {

        // arrange
        EmployeeDTO requestEmployeeDTO = TestDataFactory.newEmployeeDTO(TEST_PERSON_ID, TEST_SUBDIVISION_ID);
        EmployeeDTO expectedEmployeeDTOAnswer = TestDataFactory.newEmployeeDTO(TEST_PERSON_ID, TEST_SUBDIVISION_ID);
        EmployeeDTO expectedEmployeeEntity = TestDataFactory.newEmployeeDTO(TEST_PERSON_ID, TEST_SUBDIVISION_ID);

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
    @DisplayName("Создание Employee - Пустые person и subdivision")
    public void createEmployeeFail() {

        // arrange
        String JSONRequest = fileUtils.readFile("json-requests/create-employee-with-empty-person.json");

        ErrorDetails expectedErrorDetailsAnswer =
                ErrorDetailsFactory.newErrorDetails400Check(List.of(
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
    @DisplayName("Получение Employee по ID - Успех")
    @Sql({CREATE_PERSON_SQL_PATH, CREATE_COMPANY_SQL_PATH, CREATE_SUBDIVISION_SQL_PATH,
            CREATE_SUBDIVISIONS_COMPANIES_SQL_PATH, CREATE_EMPLOYEE_SQL_PATH})
    public void getEmployeeByIdSuccess() {

        // arrange
        EmployeeDTO expectedEmployeeDTOAnswer = TestDataFactory.newEmployeeDTO(TEST_PERSON_ID, TEST_SUBDIVISION_ID);
        expectedEmployeeDTOAnswer.setId(TEST_EMPLOYEE_ID);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(API_PATH + "/" + TEST_EMPLOYEE_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedEmployeeDTOAnswer);

    }

    @Test
    @DisplayName("Получение всех Employee")
    @Sql({CREATE_PERSON_SQL_PATH, CREATE_COMPANY_SQL_PATH, CREATE_SUBDIVISION_SQL_PATH,
            CREATE_SUBDIVISIONS_COMPANIES_SQL_PATH, CREATE_EMPLOYEE_SQL_PATH})
    public void getAllEmployeesSuccess() {

        // arrange
        EmployeeDTO employeeDTO = TestDataFactory.newEmployeeDTO(TEST_PERSON_ID, TEST_SUBDIVISION_ID);
        employeeDTO.setId(TEST_EMPLOYEE_ID);
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
