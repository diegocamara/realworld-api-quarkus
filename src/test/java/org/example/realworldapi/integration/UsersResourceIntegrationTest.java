package org.example.realworldapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.util.UserUtils;
import org.example.realworldapi.web.dto.LoginDTO;
import org.example.realworldapi.web.dto.NewUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

@QuarkusTest
@Disabled
@QuarkusTestResource(H2DatabaseTestResource.class)
public class UsersResourceIntegrationTest {

    private final String USERS_RESOURCE_PATH = "/api/users";
    private final String LOGIN_PATH = USERS_RESOURCE_PATH + "/login";

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private EntityManager entityManager;

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
    @Disabled
    @Transactional
    public void givenAValidUser_thenReturnExistingUser() throws JsonProcessingException{

        String userPassword = "123";

        User user = UserUtils.create("user1", "user1@mail.com".toUpperCase(), userPassword);

        entityManager.persist(user);
//        entityManager.flush();
//        entityManager.clear();

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
                "users.token", Matchers.notNullValue());

    }

//    @Transactional(Transactional.TxType.REQUIRES_NEW)
//    private void saveUser(User user){
//        entityManager.persist(user);
//    }

}