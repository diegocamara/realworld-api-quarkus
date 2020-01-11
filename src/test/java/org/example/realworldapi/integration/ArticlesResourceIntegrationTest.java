package org.example.realworldapi.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.AbstractIntegrationTest;
import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.Comment;
import org.example.realworldapi.domain.model.entity.Tag;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.infrastructure.web.model.request.NewArticleRequest;
import org.example.realworldapi.infrastructure.web.model.request.NewCommentRequest;
import org.example.realworldapi.infrastructure.web.model.request.UpdateArticleRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ArticlesResourceIntegrationTest extends AbstractIntegrationTest {

  private final String ARTICLES_PATH = API_PREFIX + "/articles";
  private final String FEED_PATH = ARTICLES_PATH + "/feed";

  @Test
  public void shouldReturn401WhenExecuteFeedEndpointWithoutAuthorization() {

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("offset", 0)
        .queryParam("limit", 5)
        .get(FEED_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("errors.body", hasItem("UNAUTHORIZED"));
  }

  @Test
  public void
      given10ArticlesForLoggedUser_whenExecuteFeedEndpointWithOffset0AndLimit5_shouldReturnListOf5Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User follower1 = createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123");

    List<Article> articlesFollower = createArticles(follower1, "Title", "Description", "Body", 10);

    Tag tag1 = createTag("Tag 1");

    Tag tag2 = createTag("Tag 2");

    createArticlesTags(articlesFollower, tag1, tag2);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    createArticles(user, "Title", "Description", "Body", 4);

    follow(loggedUser, follower1);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .queryParam("offset", 0)
        .queryParam("limit", 5)
        .get(FEED_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "articles[0]",
            hasKey("slug"),
            "articles[0]",
            hasKey("title"),
            "articles[0]",
            hasKey("description"),
            "articles[0]",
            hasKey("body"),
            "articles[0].tagList.size()",
            is(2),
            "articles[0].tagList",
            hasItems(tag1.getName(), tag2.getName()),
            "articles[0]",
            hasKey("createdAt"),
            "articles[0]",
            hasKey("updatedAt"),
            "articles[0]",
            hasKey("favorited"),
            "articles[0]",
            hasKey("favoritesCount"),
            "articles[0]",
            hasKey("author"),
            "articlesCount",
            is(10));
  }

  @Test
  public void
      given8ArticlesForLoggedUser_whenExecuteFeedEndpointWithOffset0AndLimit10_shouldReturnListOf8Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User follower1 = createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123");

    createArticles(follower1, "Title", "Description", "Body", 8);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    createArticles(user, "Title", "Description", "Body", 4);

    follow(loggedUser, follower1);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .queryParam("offset", 0)
        .queryParam("limit", 10)
        .get(FEED_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "articles[0]",
            hasKey("slug"),
            "articles[0]",
            hasKey("title"),
            "articles[0]",
            hasKey("description"),
            "articles[0]",
            hasKey("body"),
            "articles[0]",
            hasKey("tagList"),
            "articles[0]",
            hasKey("createdAt"),
            "articles[0]",
            hasKey("updatedAt"),
            "articles[0]",
            hasKey("favorited"),
            "articles[0]",
            hasKey("favoritesCount"),
            "articles[0]",
            hasKey("author"),
            "articlesCount",
            is(8));
  }

  @Test
  public void
      given9ArticlesForLoggedUser_whenExecuteFeedEndpointWithOffset0AndLimit10_shouldReturnListOf9Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User follower1 = createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123");

    createArticles(follower1, "Title", "Description", "Body", 5);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    createArticles(user, "Title", "Description", "Body", 4);

    follow(loggedUser, follower1);

    follow(loggedUser, user);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .queryParam("offset", 0)
        .queryParam("limit", 10)
        .get(FEED_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "articles[0]",
            hasKey("slug"),
            "articles[0]",
            hasKey("title"),
            "articles[0]",
            hasKey("description"),
            "articles[0]",
            hasKey("body"),
            "articles[0]",
            hasKey("tagList"),
            "articles[0]",
            hasKey("createdAt"),
            "articles[0]",
            hasKey("updatedAt"),
            "articles[0]",
            hasKey("favorited"),
            "articles[0]",
            hasKey("favoritesCount"),
            "articles[0]",
            hasKey("author"),
            "articlesCount",
            is(9));
  }

  @Test
  public void
      given20ArticlesForLoggedUser_whenExecuteFeedEndpointWithOffset0AndLimit10_shouldReturnListOf10Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User follower1 = createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123");

    createArticles(follower1, "Title", "Description", "Body", 2);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    createArticles(user, "Title", "Description", "Body", 18);

    follow(loggedUser, follower1);

    follow(loggedUser, user);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .queryParam("offset", 0)
        .queryParam("limit", 10)
        .get(FEED_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "articles[0]",
            hasKey("slug"),
            "articles[0]",
            hasKey("title"),
            "articles[0]",
            hasKey("description"),
            "articles[0]",
            hasKey("body"),
            "articles[0]",
            hasKey("tagList"),
            "articles[0]",
            hasKey("createdAt"),
            "articles[0]",
            hasKey("updatedAt"),
            "articles[0]",
            hasKey("favorited"),
            "articles[0]",
            hasKey("favoritesCount"),
            "articles[0]",
            hasKey("author"),
            "articlesCount",
            is(20));
  }

  @Test
  public void
      given10ArticlesWithDifferentTags_whenExecuteGlobalArticlesEndpoint_shouldReturn5Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    List<Article> articlesLoggedUser =
        createArticles(loggedUser, "Title", "Description", "Body", 5);

    Tag tag1 = createTag("Tag 1");

    createArticlesTags(articlesLoggedUser, tag1);

    articlesLoggedUser = createArticles(loggedUser, "Title", "Description", "Body", 5);

    Tag tag2 = createTag("Tag 2");

    createArticlesTags(articlesLoggedUser, tag2);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("offset", 0)
        .queryParam("limit", 10)
        .queryParam("tag", tag1.getName())
        .get(ARTICLES_PATH)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "articles[0]",
            hasKey("slug"),
            "articles[0]",
            hasKey("title"),
            "articles[0]",
            hasKey("description"),
            "articles[0]",
            hasKey("body"),
            "articles[0].tagList",
            hasItem(tag1.getName()),
            "articles[0]",
            hasKey("createdAt"),
            "articles[0]",
            hasKey("updatedAt"),
            "articles[0]",
            hasKey("favorited"),
            "articles[0]",
            hasKey("favoritesCount"),
            "articles[0]",
            hasKey("author"),
            "articlesCount",
            is(5));
  }

  @Test
  public void shouldReturn401WhenExecuteCreateArticleEndpointWithoutToken() {

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .post(ARTICLES_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED);
  }

  @Test
  public void
      givenValidArticleRequestWithoutTags_whenExecuteCreateArticleEndpoint_shouldReturnACreatedArticle()
          throws JsonProcessingException {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    NewArticleRequest newArticleRequest = createNewArticle("Title", "Description", "Body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .body(objectMapper.writeValueAsString(newArticleRequest))
        .post(ARTICLES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body(
            "article.size()",
            is(10),
            "article",
            hasKey("slug"),
            "article.title",
            is(newArticleRequest.getTitle()),
            "article.description",
            is(newArticleRequest.getDescription()),
            "article.body",
            is(newArticleRequest.getBody()),
            "article",
            hasKey("tagList"),
            "article",
            hasKey("createdAt"),
            "article",
            hasKey("updatedAt"),
            "article",
            hasKey("favorited"),
            "article",
            hasKey("favoritesCount"),
            "article",
            hasKey("author"));
  }

  @Test
  public void
      givenValidArticleRequestWithTags_whenExecuteCreateArticleEndpoint_shouldReturnACreatedArticle()
          throws JsonProcessingException {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    Tag tag1 = createTag("Tag 1");
    Tag tag2 = createTag("Tag 2");
    String tag3 = "Tag 3";
    String tag4 = "Tag 4";

    NewArticleRequest newArticleRequest =
        createNewArticle(
            "Title 1", "Description", "Body", tag1.getName(), tag2.getName(), tag3, tag4);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .body(objectMapper.writeValueAsString(newArticleRequest))
        .post(ARTICLES_PATH)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body(
            "article.size()",
            is(10),
            "article",
            hasKey("slug"),
            "article.title",
            is(newArticleRequest.getTitle()),
            "article.description",
            is(newArticleRequest.getDescription()),
            "article.body",
            is(newArticleRequest.getBody()),
            "article.tagList",
            hasItems(tag1.getName(), tag2.getName(), tag3, tag4),
            "article",
            hasKey("createdAt"),
            "article",
            hasKey("updatedAt"),
            "article",
            hasKey("favorited"),
            "article",
            hasKey("favoritesCount"),
            "article",
            hasKey("author"));
  }

  @Test
  public void
      givenExistentArticle_whenExecuteGetArticleBySlugEndpoint_shouldReturnArticleWithStatusCode200() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");
    Article article = createArticle(loggedUser, "Title", "Description", "Body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .pathParam("slug", article.getSlug())
        .get(ARTICLES_PATH + "/{slug}")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "article.title",
            is(article.getTitle()),
            "article.description",
            is(article.getDescription()),
            "article.body",
            is(article.getBody()));
  }

  @Test
  public void
      givenExistentArticle_whenExecuteUpdateArticleEndpoint_shouldReturnUpdatedArticleWithStatusCode200()
          throws JsonProcessingException {
    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");
    Article article = createArticle(loggedUser, "Title", "Description", "Body");

    UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
    updateArticleRequest.setTitle("updated title");
    updateArticleRequest.setDescription("updated description");
    updateArticleRequest.setBody("updated body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .body(objectMapper.writeValueAsString(updateArticleRequest))
        .pathParam("slug", article.getSlug())
        .put(ARTICLES_PATH + "/{slug}")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "article.title",
            is(updateArticleRequest.getTitle()),
            "article.description",
            is(updateArticleRequest.getDescription()),
            "article.body",
            is(updateArticleRequest.getBody()));
  }

  @Test
  public void givenExistentArticle_whenExecuteDeleteArticleEndpoint_shouldReturnStatusCode200() {
    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");
    Article article = createArticle(loggedUser, "Title", "Description", "Body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .pathParam("slug", article.getSlug())
        .delete(ARTICLES_PATH + "/{slug}")
        .then()
        .statusCode(HttpStatus.SC_OK);

    Assertions.assertNull(transaction(() -> entityManager.find(Article.class, article.getId())));
  }

  @Test
  public void
      givenExistentArticleWithComments_whenExecuteGetCommentsBySlugEndpoint_shouldReturnCommentWithStatusCode200() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");
    Article article = createArticle(loggedUser, "Title", "Description", "Body");

    createComment(loggedUser, article, "comment1");
    createComment(loggedUser, article, "comment2");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .pathParam("slug", article.getSlug())
        .get(ARTICLES_PATH + "/{slug}/comments")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "comments.size()",
            is(2),
            "comments[0]",
            hasKey("id"),
            "comments[0]",
            hasKey("createdAt"),
            "comments[0]",
            hasKey("updatedAt"),
            "comments[0]",
            hasKey("body"),
            "comments[0]",
            hasKey("author"));
  }

  @Test
  public void
      givenExistentArticleWithoutComments_whenExecuteCreateCommentEndpoint_shouldReturnCommentWithStatusCode200()
          throws JsonProcessingException {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");
    Article article = createArticle(loggedUser, "Title", "Description", "Body");

    NewCommentRequest newCommentRequest = new NewCommentRequest();
    newCommentRequest.setBody("comment body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .pathParam("slug", article.getSlug())
        .body(objectMapper.writeValueAsString(newCommentRequest))
        .post(ARTICLES_PATH + "/{slug}/comments")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "comment.size()",
            is(5),
            "comment",
            hasKey("id"),
            "comment",
            hasKey("createdAt"),
            "comment",
            hasKey("updatedAt"),
            "comment.body",
            is(newCommentRequest.getBody()),
            "comment.author.username",
            is(loggedUser.getUsername()));
  }

  @Test
  public void
      givenExistentArticleWithComments_whenExecuteDeleteCommentEndpoint_shouldReturnStatusCode200() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    Article article = createArticle(user, "Title", "Description", "Body");

    Comment comment1 = createComment(loggedUser, article, "comment 1 body");
    createComment(loggedUser, article, "comment 2 body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .pathParam("slug", article.getSlug())
        .pathParam("id", comment1.getId())
        .delete(ARTICLES_PATH + "/{slug}/comments/{id}")
        .then()
        .statusCode(HttpStatus.SC_OK);
  }

  @Test
  public void
      givenExistentArticle_whenExecuteFaroriteArticleEndpoint_shouldReturnFavoritedArticleWithStatusCode200() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    Article article = createArticle(user, "Title", "Description", "Body");

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .pathParam("slug", article.getSlug())
        .post(ARTICLES_PATH + "/{slug}/favorite")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "article.size()",
            is(10),
            "article",
            hasKey("slug"),
            "article.title",
            is(article.getTitle()),
            "article.description",
            is(article.getDescription()),
            "article.body",
            is(article.getBody()),
            "article",
            hasKey("tagList"),
            "article",
            hasKey("createdAt"),
            "article",
            hasKey("updatedAt"),
            "article.favorited",
            is(true),
            "article.favoritesCount",
            is(1),
            "article",
            hasKey("author"));
  }

  @Test
  public void
      givenExistentArticleFavorited_whenExecuteUnfaroriteArticleEndpoint_shouldReturnUnfavoritedArticleWithStatusCode200() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123");

    User user = createUser("user", "user@mail.com", "bio", "image", "user123");

    Article article = createArticle(user, "Title", "Description", "Body");

    favorite(article, loggedUser);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .pathParam("slug", article.getSlug())
        .delete(ARTICLES_PATH + "/{slug}/favorite")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body(
            "article.size()",
            is(10),
            "article",
            hasKey("slug"),
            "article.title",
            is(article.getTitle()),
            "article.description",
            is(article.getDescription()),
            "article.body",
            is(article.getBody()),
            "article",
            hasKey("tagList"),
            "article",
            hasKey("createdAt"),
            "article",
            hasKey("updatedAt"),
            "article.favorited",
            is(false),
            "article.favoritesCount",
            is(0),
            "article",
            hasKey("author"));
  }

  private NewArticleRequest createNewArticle(
      String title, String description, String body, String... tagList) {
    NewArticleRequest newArticleRequest = new NewArticleRequest();
    newArticleRequest.setTitle(title);
    newArticleRequest.setDescription(description);
    newArticleRequest.setBody(body);
    newArticleRequest.setTagList(Arrays.asList(tagList));
    return newArticleRequest;
  }
}
