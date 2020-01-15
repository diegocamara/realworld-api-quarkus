package org.example.realworldapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import org.example.realworldapi.domain.model.builder.ArticleBuilder;
import org.example.realworldapi.domain.model.entity.*;
import org.example.realworldapi.domain.model.provider.TokenProvider;
import org.example.realworldapi.util.UserUtils;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class AbstractIntegrationTest extends DatabaseIntegrationTest {

  @Inject protected ObjectMapper objectMapper;
  @Inject protected TokenProvider tokenProvider;
  @Inject protected Slugify slugify;

  @BeforeEach
  public void beforeEach() {
    clear();
  }

  protected User createUser(
      String username, String email, String bio, String image, String password) {
    return transaction(
        () -> {
          User user = UserUtils.create(username, email, password, bio, image);
          entityManager.persist(user);
          user.setToken(tokenProvider.createUserToken(user.getId().toString()));
          entityManager.merge(user);
          return user;
        });
  }

  protected void follow(User currentUser, User... followers) {

    transaction(
        () -> {
          User user = entityManager.find(User.class, currentUser.getId());

          for (User follower : followers) {
            UsersFollowedKey key = new UsersFollowedKey();
            key.setUser(user);
            key.setFollowed(follower);

            UsersFollowed usersFollowed = new UsersFollowed();
            usersFollowed.setPrimaryKey(key);
            entityManager.persist(usersFollowed);
          }

          entityManager.persist(user);
        });
  }

  protected Tag createTag(String name) {
    return transaction(
        () -> {
          Tag tag = new Tag(name);
          entityManager.persist(tag);
          return tag;
        });
  }

  protected List<ArticlesTags> createArticlesTags(List<Article> articles, Tag... tags) {
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
  };

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
