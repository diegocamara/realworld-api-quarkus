package org.example.realworldapi.integration;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.AbstractIntegrationTest;
import org.example.realworldapi.domain.model.entity.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.API_PREFIX;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class TagsResourceIntegrationTest extends AbstractIntegrationTest {

  private final String TAGS_PATH = API_PREFIX + "/tags";

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
}
