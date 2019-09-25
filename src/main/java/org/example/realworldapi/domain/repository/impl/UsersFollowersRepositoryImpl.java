package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.entity.persistent.User;
import org.example.realworldapi.domain.entity.persistent.UsersFollowers;
import org.example.realworldapi.domain.entity.persistent.UsersFollowersKey;
import org.example.realworldapi.domain.repository.UsersFollowersRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class UsersFollowersRepositoryImpl
    extends AbstractRepository<UsersFollowers, UsersFollowersKey>
    implements UsersFollowersRepository {

  private EntityManager entityManager;

  public UsersFollowersRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<UsersFollowers> getEntityClass() {
    return UsersFollowers.class;
  }

  @Override
  public boolean isFollowing(Long currentUserId, Long followerUserId) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<UsersFollowers> usersFollowers = getRoot(criteriaQuery, UsersFollowers.class);
    criteriaQuery.select(builder.count(usersFollowers));
    criteriaQuery.where(
        builder.and(
            builder.equal(usersFollowers.get("primaryKey").get("user").get("id"), currentUserId),
            builder.equal(
                usersFollowers.get("primaryKey").get("follower").get("id"), followerUserId)));
    return getSingleResult(criteriaQuery).intValue() > 0;
  }

  @Override
  public UsersFollowers findByKey(UsersFollowersKey primaryKey) {
    return entityManager.find(UsersFollowers.class, primaryKey);
  }

  @Override
  public UsersFollowers insertOrUpdate(UsersFollowers usersFollowers) {
    entityManager.merge(usersFollowers);
    return usersFollowers;
  }

  @Override
  public void delete(UsersFollowers usersFollowers) {
    entityManager.remove(usersFollowers);
  }

  @Override
  public List<Article> findMostRecentArticles(Long loggedUserId, int offset, int limit) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Article> criteriaQuery = getCriteriaQuery(builder, Article.class);
    Root<UsersFollowers> usersFollowers = getRoot(criteriaQuery, UsersFollowers.class);

    Join<UsersFollowers, User> user = usersFollowers.join("primaryKey").join("user");

    user.on(builder.equal(user.get("id"), loggedUserId));

    Join<UsersFollowers, User> follower = usersFollowers.join("primaryKey").join("follower");

    Join<User, Article> articles = follower.join("articles");

    criteriaQuery.select(articles);

    criteriaQuery.orderBy(builder.desc(articles.get("updatedAt")));

    return getPagedResultList(criteriaQuery, offset, limit);
  }
}
