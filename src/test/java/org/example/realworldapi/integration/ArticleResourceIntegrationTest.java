package org.example.realworldapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.example.realworldapi.DatabaseIntegrationTest;
import org.example.realworldapi.domain.builder.ArticleBuilder;
import org.example.realworldapi.domain.entity.persistent.*;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.service.JWTService;
import org.example.realworldapi.util.InsertResult;
import org.example.realworldapi.util.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.realworldapi.constants.TestConstants.*;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ArticleResourceIntegrationTest extends DatabaseIntegrationTest {

  private final String ARTICLES_PATH = API_PREFIX + "/articles";
  private final String FEED_PATH = ARTICLES_PATH + "/feed";

  @Inject private ObjectMapper objectMapper;
  @Inject private JWTService jwtService;

  @BeforeEach
  public void beforeEach() {
    clear();
  }

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
  public void givenInvalidOffsetAndLimitShouldReturn422() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123", Role.USER);

    given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + loggedUser.getToken())
        .get(FEED_PATH)
        .then()
        .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
        .body("errors.body", hasItem("limit parameter must be at least 1"));
  }

  @Test
  public void
      given10ArticlesForLoggedUser_whenExecuteFeedEndpointWithOffset0AndLimit5_shouldReturnListOf5Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123", Role.USER);

    User follower1 =
        createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123", Role.USER);

    InsertResult<Article> insertResult = new InsertResult<>();

    createArticles(follower1, "Title", "Description", "Body", 10, insertResult);

    Tag tag1 = createTag("Tag 1");

    Tag tag2 = createTag("Tag 2");

    createArticlesTags(insertResult.getResults(), tag1, tag2);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123", Role.USER);

    createArticles(user, "Title", "Description", "Body", 4, insertResult);

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
            is(5));
  }

  @Test
  public void
      given8ArticlesForLoggedUser_whenExecuteFeedEndpointWithOffset0AndLimit10_shouldReturnListOf8Articles() {

    User loggedUser =
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123", Role.USER);

    User follower1 =
        createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123", Role.USER);

    InsertResult<Article> insertResult = new InsertResult<>();

    createArticles(follower1, "Title", "Description", "Body", 8, insertResult);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123", Role.USER);

    createArticles(user, "Title", "Description", "Body", 4, insertResult);

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
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123", Role.USER);

    User follower1 =
        createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123", Role.USER);

    InsertResult<Article> insertResult = new InsertResult<>();

    createArticles(follower1, "Title", "Description", "Body", 5, insertResult);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123", Role.USER);

    createArticles(user, "Title", "Description", "Body", 4, insertResult);

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
        createUser("loggedUser", "loggeduser@mail.com", "bio", "image", "loggeduser123", Role.USER);

    User follower1 =
        createUser("follower1", "follower1@mail.com", "bio", "image", "follower1_123", Role.USER);

    InsertResult<Article> insertResult = new InsertResult<>();

    createArticles(follower1, "Title", "Description", "Body", 2, insertResult);

    User user = createUser("user", "user@mail.com", "bio", "image", "user123", Role.USER);

    createArticles(user, "Title", "Description", "Body", 18, insertResult);

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
            is(10));
  }

  private void createArticles(
      User author,
      String title,
      String description,
      String body,
      int quantity,
      InsertResult<Article> insertResult) {

    transaction(
        () -> {
          for (int articleIndex = 0; articleIndex < quantity; articleIndex++) {

            Article article =
                new ArticleBuilder()
                    .title(title + "_" + articleIndex)
                    .description(description + "_" + articleIndex)
                    .body(body + "_" + articleIndex)
                    .build();
            int id = insertResult.add(article);
            article.setId((long) id);
            Date date = new Date();

            entityManager
                .createNativeQuery(
                    "INSERT INTO ARTICLES (ID, TITLE, DESCRIPTION, BODY, CREATEDAT, UPDATEDAT, AUTHOR_ID) VALUES (?, ?, ?, ?, ?, ?, ?)")
                .setParameter(1, id)
                .setParameter(2, article.getTitle())
                .setParameter(3, article.getDescription())
                .setParameter(4, article.getBody())
                .setParameter(5, date, TemporalType.TIMESTAMP)
                .setParameter(6, date, TemporalType.TIMESTAMP)
                .setParameter(7, author.getId())
                .executeUpdate();
          }
        });
  }

  private void follow(User currentUser, User... followers) {

    transaction(
        () -> {
          User user = entityManager.find(User.class, currentUser.getId());

          for (User follower : followers) {
            UsersFollowersKey key = new UsersFollowersKey();
            key.setUser(user);
            key.setFollower(follower);

            UsersFollowers usersFollowers = new UsersFollowers();
            usersFollowers.setPrimaryKey(key);
            entityManager.persist(usersFollowers);
          }

          entityManager.persist(user);
        });
  }

  private Tag createTag(String name) {
    return transaction(
        () -> {
          Tag tag = new Tag();
          tag.setName(name);
          entityManager.persist(tag);
          return tag;
        });
  }

  private List<ArticlesTags> createArticlesTags(List<Article> articles, Tag... tags) {
    return transaction(
        () -> {
          List<ArticlesTags> resultList = new LinkedList<>();

          for (Article article : articles) {

            Article managedArticle = entityManager.find(Article.class, article.getId());

            for (Tag tag : tags) {
              Tag managedTag = entityManager.find(Tag.class, tag.getId());

              ArticlesTagsKey articlesTagsKey = new ArticlesTagsKey();
              articlesTagsKey.setArticle(managedArticle);
              articlesTagsKey.setTag(managedTag);

              ArticlesTags articlesTags = new ArticlesTags();
              articlesTags.setPrimaryKey(articlesTagsKey);

              entityManager.persist(articlesTags);
              resultList.add(articlesTags);
            }
          }

          return resultList;
        });
  }

  private User createUser(
      String username, String email, String bio, String image, String password, Role... role) {
    return transaction(
        () -> {
          User user = UserUtils.create(username, email, password, bio, image);
          entityManager.persist(user);
          user.setToken(jwtService.sign(user.getId().toString(), role));
          entityManager.merge(user);
          return user;
        });
  }
}
