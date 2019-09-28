package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.persistent.*;
import org.example.realworldapi.domain.repository.ArticleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticleRepositoryImpl extends AbstractRepository<Article, Long>
    implements ArticleRepository {

  private EntityManager entityManager;

  public ArticleRepositoryImpl(EntityManager entityManager) {
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
  public Article create(Article article) {
    this.entityManager.persist(article);
    return article;
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
  public Article update(Article article) {
    return entityManager.merge(article);
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
