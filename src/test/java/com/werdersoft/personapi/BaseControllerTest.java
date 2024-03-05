package com.werdersoft.personapi;

import com.werdersoft.personapi.utils.DBQueriesUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class BaseControllerTest {

    protected static final UUID TEST_PERSON_ID = UUID.fromString("8edc1f88-7bc5-4814-91b3-abd6daeba0c0");
    protected static final UUID TEST_COMPANY_ID = UUID.fromString("2c1b37ff-a629-4bc3-8496-91c143540924");
    protected static final UUID TEST_SUBDIVISION_ID = UUID.fromString("989a4f41-0af7-45d2-a32e-0f99b1241b29");
    protected static final UUID TEST_EMPLOYEE_ID = UUID.fromString("3140e0f8-3d51-4bce-9ac0-347ef0ded50f");

    protected static final String CREATE_PERSON_SQL_PATH = "/db-requests/create-test-person.sql";
    protected static final String CREATE_COMPANY_SQL_PATH = "/db-requests/create-test-company.sql";
    protected static final String CREATE_SUBDIVISION_SQL_PATH = "/db-requests/create-test-subdivision.sql";
    protected static final String CREATE_SUBDIVISIONS_COMPANIES_SQL_PATH =
            "/db-requests/create-test-subdivisions-companies.sql";
    protected static final String CREATE_EMPLOYEE_SQL_PATH = "/db-requests/create-test-employee.sql";

    @Autowired
    protected DBQueriesUtils dbQueriesUtils;

    @Autowired
    protected WebTestClient webTestClient;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        container.start();
        var s = container.getJdbcUrl();
    }

    @AfterEach
    void tearDown() {
        dbQueriesUtils.deleteAllRecords();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

}
