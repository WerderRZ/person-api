package com.werdersoft.personapi.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Тесты rest-контроллера PersonController")
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PersonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private PersonService personService;

    private String apiPath;

    @BeforeEach
    void beforeEach() {
        apiPath = "/api/v1/persons";
    }

    @Test
    @DisplayName("GET - получение Person по ID")
    public void getPersonById() throws Exception {

        // arrange
        UUID id = UUID.randomUUID();

        PersonDTO expectedPersonDTO = new PersonDTO();
        expectedPersonDTO.setId(id);
        expectedPersonDTO.setName("Sam");
        expectedPersonDTO.setAge(22);

        // act
        var actualResponseDTO = webTestClient
                .get()
                .uri(apiPath + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        //assertThat(actualResponseDTO).isNotNull();
        //assertThat(actualResponseDTO.getName()).isNotBlank().isEqualTo(expectedPersonDTO.getName());
        //assertThat(actualResponseDTO.getAge()).isEqualTo(expectedPersonDTO.getAge());

    }

    @Test
    @DisplayName("POST - cоздание Person")
    public void createPerson() throws Exception {

        // arrange
        PersonDTO actualPersonDTO = new PersonDTO();
        actualPersonDTO.setName("Sam");
        actualPersonDTO.setAge(22);

        PersonDTO expectedPersonDTO = new PersonDTO();
        expectedPersonDTO.setId(UUID.randomUUID());
        expectedPersonDTO.setName("Sam");
        expectedPersonDTO.setAge(22);

        //when(personService.createPerson(actualPersonDTO)).thenReturn(expectedPersonDTO);

        // act
        var actualResponseDTO = webTestClient
                .post()
                .uri(apiPath)
                .body(Mono.just(actualPersonDTO), PersonDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PersonDTO.class)
                .returnResult()
                .getResponseBody();

        // assert
        assertThat(actualResponseDTO).isEqualTo(expectedPersonDTO);

    }

}
