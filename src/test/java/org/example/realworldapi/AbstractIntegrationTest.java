package org.example.realworldapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import org.example.realworldapi.domain.model.builder.ArticleBuilder;
import org.example.realworldapi.domain.model.entity.*;
import org.example.realworldapi.domain.model.provider.TokenProvider;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.*;
import org.example.realworldapi.util.UserEntityUtils;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AbstractIntegrationTest extends DatabaseIntegrationTest {

  @Inject protected ObjectMapper objectMapper;
  @Inject protected TokenProvider tokenProvider;
  @Inject protected Slugify slugify;

  @BeforeEach
  public void beforeEach() {
    clear();
  }

  protected UserEntity createUserEntity(
      String username, String email, String bio, String image, String password) {
    return transaction(
        () -> {
          final var userEntity = UserEntityUtils.create(username, email, password, bio, image);
          entityManager.persist(userEntity);
          return userEntity;
        });
  }

  protected String token(UserEntity userEntity) {
    return tokenProvider.createUserToken(userEntity.getId().toString());
  }

  protected void follow(UserEntity currentUser, UserEntity... followers) {

    transaction(
        () -> {
          final var user = entityManager.find(UserEntity.class, currentUser.getId());

          for (UserEntity follower : followers) {
            FollowRelationshipEntityKey key = new FollowRelationshipEntityKey();
            key.setUser(user);
            key.setFollowed(follower);

            FollowRelationshipEntity followRelationshipEntity = new FollowRelationshipEntity();
            followRelationshipEntity.setPrimaryKey(key);
            entityManager.persist(followRelationshipEntity);
          }

          entityManager.persist(user);
        });
  }

  protected TagEntity createTagEntity(String name) {
    return transaction(
        () -> {
          final var tag = new TagEntity();
          tag.setId(UUID.randomUUID());
          tag.setName(name);
          entityManager.persist(tag);
          return tag;
        });
  }

  protected List<TagRelationshipEntity> createArticlesTags(
      List<ArticleEntity> articles, TagEntity... tags) {
    return transaction(
        () -> {
          final var resultList = new LinkedList<TagRelationshipEntity>();

          for (ArticleEntity article : articles) {

            final var managedArticle = entityManager.find(ArticleEntity.class, article.getId());

            for (TagEntity tag : tags) {
              final var managedTag = entityManager.find(TagEntity.class, tag.getId());

              final var articlesTagsEntityKey = new TagRelationshipEntityKey();
              articlesTagsEntityKey.setArticle(managedArticle);
              articlesTagsEntityKey.setTag(managedTag);

              final var articlesTagsEntity = new TagRelationshipEntity();
              articlesTagsEntity.setPrimaryKey(articlesTagsEntityKey);

              entityManager.persist(articlesTagsEntity);
              resultList.add(articlesTagsEntity);
            }
          }

          return resultList;
        });
  }

  protected List<Article> createArticles(
      User author, String title, String description, String body, int quantity) {

    List<Article> articles = new LinkedList<>();

    for (int articleIndex = 0; articleIndex < quantity; articleIndex++) {
      articles.add(
          createArticle(
              author,
              title + "_" + articleIndex,
              description + "_" + articleIndex,
              body + "_" + articleIndex));
    }

    return articles;
  }

  protected Article createArticle(User author, String title, String description, String body) {
    return transaction(
        () -> {
          Article article =
              new ArticleBuilder()
                  .title(title)
                  .slug(slugify.slugify(title))
                  .description(description)
                  .body(body)
                  .author(author)
                  .build();
          entityManager.persist(article);
          return article;
        });
  }

  protected ArticlesUsers favorite(Article article, User user) {
    return transaction(
        () -> {
          ArticlesUsers articlesUsers = getArticlesUsers(article, user);
          entityManager.persist(articlesUsers);
          return articlesUsers;
        });
  }

  protected Comment createComment(User author, Article article, String body) {
    return transaction(
        () -> {
          Comment comment = new Comment();
          comment.setBody(body);
          comment.setArticle(article);
          comment.setAuthor(author);
          entityManager.persist(comment);
          return comment;
        });
  }
  ;

  private ArticlesUsers getArticlesUsers(Article article, User loggedUser) {
    ArticlesUsersKey articlesUsersKey = getArticlesUsersKey(article, loggedUser);
    ArticlesUsers articlesUsers = new ArticlesUsers();
    articlesUsers.setPrimaryKey(articlesUsersKey);
    return articlesUsers;
  }

  private ArticlesUsersKey getArticlesUsersKey(Article article, User loggedUser) {
    ArticlesUsersKey articlesUsersKey = new ArticlesUsersKey();
    articlesUsersKey.setArticle(article);
    articlesUsersKey.setUser(loggedUser);
    return articlesUsersKey;
  }
}
