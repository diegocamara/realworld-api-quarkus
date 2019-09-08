package org.example.realworldapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.DatabaseIntegrationTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.util.UserUtils;
import org.example.realworldapi.web.dto.LoginDTO;
import org.example.realworldapi.web.dto.NewUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class UsersResourceIntegrationTest extends DatabaseIntegrationTest {

    private final String USERS_RESOURCE_PATH = "/api/users";
    private final String LOGIN_PATH = USERS_RESOURCE_PATH + "/login";

    @Inject
    private ObjectMapper objectMapper;

    @AfterEach
    public void afterEach(){
        databaseCleanner.clear();
    }

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
                .body("user.id", Matchers.nullValue(),
                        "user.password", Matchers.nullValue(),
                        "user.username", Matchers.notNullValue(),
                        "user.email", Matchers.notNullValue(),
                        "user.token", Matchers.notNullValue());

    }

    @Test
    public void givenAPersistedUser_whenCallingRegisterUserEndpointWithExistingEmail_thenReturnConflitWithCode409() throws JsonProcessingException {

        String userPassword = "123";

        User user = UserUtils.create("user1", "user1@mail.com", userPassword);

        transaction(() -> entityManager.persist(user));

        NewUserDTO newUser = new NewUserDTO();
        newUser.setUsername("user2");
        newUser.setEmail(user.getEmail());
        newUser.setPassword("user123");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(newUser))
                .when()
                .post(USERS_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body(is("Conflict"));

    }

    @Test
    public void givenAnInvalidUser_thenReturnErrorsWith422Code() throws JsonProcessingException{

        NewUserDTO newUser = new NewUserDTO();
        newUser.setEmail("user@mail.com");
        newUser.setPassword("user123");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(newUser))
                .when()
                .post(USERS_RESOURCE_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors.body", hasItems("username must be not blank"));

    }

    @Test
    public void givenAValidLogin_whenExecuteLoginEndpoint_shouldReturnExistingUser() throws JsonProcessingException{

        String userPassword = "123";

        User user = UserUtils.create("user1", "user1@mail.com", userPassword);

        transaction(() -> entityManager.persist(user));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(user.getEmail());
        loginDTO.setPassword(userPassword);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(loginDTO))
        .when()
        .post(LOGIN_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("user.id", Matchers.nullValue(),
                "user.password", Matchers.nullValue(),
                "user.username", Matchers.notNullValue(),
                "user.email", Matchers.notNullValue(),
                "user.token", Matchers.notNullValue());

    }

    @Test
    public void givenAInvalidLogin_whenExecuteLoginEndpoint_shouldReturnErrorsWith422Code() throws JsonProcessingException{

        LoginDTO loginDTO = new LoginDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
                .body("errors.body",hasItems("email must be not blank", "password must be not blank"));

    }

    @Test
    public void givenAInvalidLoginEmail_whenExecuteLoginEndpoint_shouldReturnUnauthorized() throws JsonProcessingException{

        String userPassword = "123";

        User user = UserUtils.create("user1", "user1@mail.com", userPassword);

        transaction(() -> entityManager.persist(user));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("user2@mail.com");
        loginDTO.setPassword(userPassword);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(is("Unauthorized"));

    }

    @Test
    public void givenAInvalidLoginPassword_whenExecuteLoginEndpoint_shouldReturnUnauthorized() throws JsonProcessingException{

        User user = UserUtils.create("user1", "user1@mail.com", "123");

        transaction(() -> entityManager.persist(user));

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(user.getEmail());
        loginDTO.setPassword("145");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(loginDTO))
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(is("Unauthorized"));

    }

}