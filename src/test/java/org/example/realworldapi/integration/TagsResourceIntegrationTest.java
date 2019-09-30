package org.example.realworldapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.DatabaseIntegrationTest;
import org.example.realworldapi.domain.entity.persistent.Tag;
import org.example.realworldapi.domain.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.API_PREFIX;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class TagsResourceIntegrationTest extends DatabaseIntegrationTest {

  private final String TAGS_PATH = API_PREFIX + "/tags";

  @Inject private ObjectMapper objectMapper;
  @Inject private JWTService jwtService;
  @Inject private Slugify slugify;

  @BeforeEach
  public void beforeEach() {
    clear();
  }

  @Test
  public void givenExistentTags_whenExecuteGetTagsEndpoint_shouldReturnTagListWithStatusCode200() {

    Tag tag1 = createTag("tag 1");
    Tag tag2 = createTag("tag 2");
    Tag tag3 = createTag("tag 3");
    Tag tag4 = createTag("tag 4");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .get(TAGS_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "tags.size()",
            is(4),
            "tags",
            hasItems(tag1.getName(), tag2.getName(), tag3.getName(), tag4.getName()));
  }

  private Tag createTag(String name) {
    return transaction(
        () -> {
          Tag tag = new Tag(name);
          entityManager.persist(tag);
          return tag;
        });
  }
}
