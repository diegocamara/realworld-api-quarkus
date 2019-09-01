package org.example.realworldapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.web.dto.NewUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class UsersResourceTest {

    private final String USERS_RESOURCE_PATH = "/api/users";

    @Inject
    private ObjectMapper objectMapper;

    @Test
    public void givenAValidUser_whenCallingRegisterUserEndpoint_thenReturnAnUserWithTokenFieldAndCode201() throws JsonProcessingException {

        NewUserDTO newUser = new NewUserDTO();
        newUser.setUsername("user");
        newUser.setEmail("user@mail.com");
        newUser.setPassword("user123");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(newUser))
                .when()
                .post(USERS_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("user.token", Matchers.notNullValue());

    }

}