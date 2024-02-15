package com.werdersoft.personapi.employee;

import com.werdersoft.personapi.company.CompanyDTO;
import com.werdersoft.personapi.company.CompanyService;
import com.werdersoft.personapi.enums.Position;
import com.werdersoft.personapi.enums.Region;
import com.werdersoft.personapi.person.PersonDTO;
import com.werdersoft.personapi.person.PersonService;
import com.werdersoft.personapi.subdivision.SubdivisionDTO;
import com.werdersoft.personapi.subdivision.SubdivisionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера EmployeeController")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PersonService personService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SubdivisionService subdivisionService;

    @Autowired
    private EmployeeService employeeService;

    private String apiPath;

    private PersonDTO testPersonDTO;

    private SubdivisionDTO testSubdivisionDTO;

    @BeforeAll
    void init() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Werdersoft");
        companyDTO.setRegion(Region.ASIA);
        CompanyDTO testCompanyDTO = companyService.createCompany(companyDTO);
        List<UUID> companiesIds = new ArrayList<>();
        companiesIds.add(testCompanyDTO.getId());

        SubdivisionDTO subdivisionDTO = new SubdivisionDTO();
        subdivisionDTO.setName("IT");
        subdivisionDTO.setCompaniesIds(companiesIds);
        testSubdivisionDTO = subdivisionService.createSubdivision(subdivisionDTO);
    }

    @BeforeEach
    void prepare() {
        apiPath = "/api/v1/employees";

        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Sam");
        personDTO.setAge(20);
        testPersonDTO = personService.createPerson(personDTO);
    }

    @Test
    @DisplayName("POST - Успешное cоздание Employee")
    public void createEmployeeSuccess() throws Exception {

        // arrange
        EmployeeDTO actualEmployeeDTO = getTestEmployeeDTO();
        UUID personId = testPersonDTO.getId();
        UUID subdivisionId = testSubdivisionDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualEmployeeDTO), EmployeeDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isNotNull();
        assertThat(actualResponseDTO.getPosition()).isEqualTo(actualEmployeeDTO.getPosition());
        assertThat(actualResponseDTO.getPersonId()).isEqualTo(personId);
        assertThat(actualResponseDTO.getSubdivisionId()).isEqualTo(subdivisionId);

    }

    @Test
    @DisplayName("GET - Успешное получение Employee по ID")
    public void getEmployeeByIdSuccess() throws Exception {

        // arrange
        EmployeeDTO employeeDTO = getTestEmployeeDTO();
        EmployeeDTO expectedEmployeeDTO = employeeService.createEmployee(employeeDTO);
        UUID id = expectedEmployeeDTO.getId();

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedEmployeeDTO);

    }



    private EmployeeDTO getTestEmployeeDTO() {
        EmployeeDTO testEmployeeDTO = new EmployeeDTO();
        testEmployeeDTO.setPosition(Position.MANAGER);
        testEmployeeDTO.setSalary(new BigDecimal(300));
        testEmployeeDTO.setPersonId(testPersonDTO.getId());
        testEmployeeDTO.setSubdivisionId(testSubdivisionDTO.getId());
        return testEmployeeDTO;
    }


}
