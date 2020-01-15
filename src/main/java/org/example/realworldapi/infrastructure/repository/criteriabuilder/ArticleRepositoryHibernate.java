package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.*;
import org.example.realworldapi.domain.model.repository.ArticleRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Deprecated
public class ArticleRepositoryHibernate extends AbstractRepositoryHibernate<Article, Long>
    implements ArticleRepository {

  private EntityManager entityManager;

  public ArticleRepositoryHibernate(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public List<Article> findArticles(
      int offset, int limit, List<String> tags, List<String> authors, List<String> favorited) {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Article> criteriaQuery = getCriteriaQuery(builder);
    Root<Article> article = getRoot(criteriaQuery);

    criteriaQuery.select(article);

    List<Predicate> predicates = new LinkedList<>();

    if (isNotEmpty(tags)) {
      ListJoin<Article, ArticlesTags> articlesTags = article.joinList("tags");
      Join<ArticlesTags, Tag> tag = articlesTags.join("primaryKey").join("tag");
      predicates.add(builder.upper(tag.get("name")).in(toUpperCase(tags)));
    }

    if (isNotEmpty(authors)) {
      Join<Article, User> author = article.join("author");
      predicates.add(builder.upper(author.get("username")).in(toUpperCase(authors)));
    }

    if (isNotEmpty(favorited)) {
      ListJoin<Article, ArticlesUsers> articlesUsers = article.joinList("favorites");
      Join<ArticlesUsers, User> userWhoFavorited = articlesUsers.join("primaryKey").join("user");
      predicates.add(builder.upper(userWhoFavorited.get("username")).in(toUpperCase(favorited)));
    }

    criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));

    criteriaQuery.orderBy(builder.desc(article.get("updatedAt")));

    return getPagedResultList(criteriaQuery, offset, limit);
  }

  @Override
  public long count(List<String> tags, List<String> authors, List<String> favorited) {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<Article> article = getRoot(criteriaQuery, Article.class);

    criteriaQuery.select(builder.count(article));

    List<Predicate> predicates = new LinkedList<>();

    if (isNotEmpty(tags)) {
      ListJoin<Article, ArticlesTags> articlesTags = article.joinList("tags");
      Join<ArticlesTags, Tag> tag = articlesTags.join("primaryKey").join("tag");
      predicates.add(builder.upper(tag.get("name")).in(toUpperCase(tags)));
    }

    if (isNotEmpty(authors)) {
      Join<Article, User> author = article.join("author");
      predicates.add(builder.upper(author.get("username")).in(toUpperCase(authors)));
    }

    if (isNotEmpty(favorited)) {
      ListJoin<Article, ArticlesUsers> articlesUsers = article.joinList("favorites");
      Join<ArticlesUsers, User> userWhoFavorited = articlesUsers.join("primaryKey").join("user");
      predicates.add(builder.upper(userWhoFavorited.get("username")).in(toUpperCase(favorited)));
    }

    criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));

    return getSingleResult(criteriaQuery);
  }

  @Override
  public long count(Long loggedUserId) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<Article> articles = getRoot(criteriaQuery, Article.class);
    Join<Article, User> author = articles.join("author");
    ListJoin<User, UsersFollowed> followedBy = author.joinList("followedBy");
    criteriaQuery.select(builder.count(articles));
    criteriaQuery.where(builder.equal(followedBy.get("user").get("id"), loggedUserId));
    return getSingleResult(criteriaQuery);
  }

  @Override
  public Article create(Article article) {
    return persist(article);
  }

  @Override
  public boolean existsBySlug(String slug) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<Article> article = getRoot(criteriaQuery, Article.class);

    criteriaQuery.select(builder.count(article));
    criteriaQuery.where(
        builder.equal(builder.upper(article.get("slug")), slug.toUpperCase().trim()));

    return getSingleResult(criteriaQuery).intValue() > 0;
  }

  @Override
  public Optional<Article> findBySlug(String slug) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Article> criteriaQuery = getCriteriaQuery(builder);
    Root<Article> article = getRoot(criteriaQuery);
    criteriaQuery.select(article);
    criteriaQuery.where(
        builder.equal(builder.upper(article.get("slug")), slug.toUpperCase().trim()));
    return Optional.ofNullable(getSingleResult(criteriaQuery));
  }

  @Override
  public void remove(Article article) {
    entityManager.remove(article);
  }

  @Override
  public Optional<Article> findByIdAndSlug(Long authorId, String slug) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Article> criteriaQuery = getCriteriaQuery(builder);
    Root<Article> article = getRoot(criteriaQuery);
    Join<Article, User> author = article.join("author");
    criteriaQuery.select(article);
    criteriaQuery.where(
        builder.and(
            builder.equal(author.get("id"), authorId),
            builder.equal(builder.upper(article.get("slug")), slug.toUpperCase().trim())));
    return Optional.ofNullable(getSingleResult(criteriaQuery));
  }

  @Override
  public List<Article> findMostRecentArticles(Long loggedUserId, int offset, int limit) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Article> criteriaQuery = getCriteriaQuery(builder);
    Root<Article> articles = getRoot(criteriaQuery);
    Join<Article, User> author = articles.join("author");
    ListJoin<User, UsersFollowed> followedBy = author.joinList("followedBy");
    criteriaQuery.select(articles);
    criteriaQuery.where(builder.equal(followedBy.get("user").get("id"), loggedUserId));
    criteriaQuery.orderBy(builder.desc(articles.get("updatedAt")));
    return getPagedResultList(criteriaQuery, offset, limit);
  }

  private List<String> toUpperCase(List<String> tags) {
    return tags.stream().map(String::toUpperCase).collect(Collectors.toList());
  }

  private boolean isNotEmpty(List<?> list) {
    return list != null && !list.isEmpty();
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<Article> getEntityClass() {
    return Article.class;
  }
}
