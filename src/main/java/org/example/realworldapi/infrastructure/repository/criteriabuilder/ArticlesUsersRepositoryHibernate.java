package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.ArticlesUsers;
import org.example.realworldapi.domain.model.entity.ArticlesUsersKey;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.repository.ArticlesUsersRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Optional;

@Deprecated
public class ArticlesUsersRepositoryHibernate
    extends AbstractRepositoryHibernate<ArticlesUsers, ArticlesUsersKey>
    implements ArticlesUsersRepository {

  private EntityManager entityManager;

  public ArticlesUsersRepositoryHibernate(EntityManager entityManager) {
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
  public long favoritesCount(Long articleId) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<ArticlesUsers> articlesUsers = getRoot(criteriaQuery, ArticlesUsers.class);
    Path<ArticlesUsersKey> articlesUsersKey = articlesUsers.get("primaryKey");
    Path<Article> article = articlesUsersKey.get("article");
    criteriaQuery.select(builder.count(articlesUsers));
    criteriaQuery.where(builder.equal(article.get("id"), articleId));
    return getSingleResult(criteriaQuery);
  }

  @Override
  public ArticlesUsers create(ArticlesUsers articlesUsers) {
    return persist(articlesUsers);
  }

  @Override
  public Optional<ArticlesUsers> findById(ArticlesUsersKey articlesUsersKey) {
    return Optional.ofNullable(entityManager.find(ArticlesUsers.class, articlesUsersKey));
  }

  @Override
  public void remove(ArticlesUsers articlesUsers) {
    entityManager.remove(articlesUsers);
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
