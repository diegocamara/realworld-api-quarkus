package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.entity.persistent.ArticlesUsers;
import org.example.realworldapi.domain.entity.persistent.ArticlesUsersKey;
import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.domain.repository.ArticlesUsersRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

@ApplicationScoped
public class ArticlesUsersRepositoryImpl extends AbstractRepository<ArticlesUsers, ArticlesUsersKey>
    implements ArticlesUsersRepository {

  private EntityManager entityManager;

  public ArticlesUsersRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public boolean isFavorited(Long articleId, Long currentUserId) {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<ArticlesUsers> articlesUsers = getRoot(criteriaQuery, ArticlesUsers.class);
    criteriaQuery.select(builder.count(articlesUsers));

    Path<ArticlesUsersKey> articlesUsersKey = articlesUsers.get("primaryKey");
    Path<Article> article = articlesUsersKey.get("article");
    Path<User> user = articlesUsersKey.get("user");

    Predicate predicate =
        builder.and(
            builder.equal(article.get("id"), articleId),
            builder.equal(user.get("id"), currentUserId));

    criteriaQuery.where(predicate);

    return getSingleResult(criteriaQuery).intValue() > 0;
  }

  @Override
  public int favoritesCount(Long articleId) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<ArticlesUsers> articlesUsers = getRoot(criteriaQuery, ArticlesUsers.class);
    Path<ArticlesUsersKey> articlesUsersKey = articlesUsers.get("primaryKey");
    Path<Article> article = articlesUsersKey.get("article");
    criteriaQuery.select(builder.count(articlesUsers));
    criteriaQuery.where(builder.equal(article.get("id"), articleId));
    return getSingleResult(criteriaQuery).intValue();
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<ArticlesUsers> getEntityClass() {
    return ArticlesUsers.class;
  }
}
