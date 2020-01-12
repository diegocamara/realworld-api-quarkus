package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.entity.UsersFollowers;
import org.example.realworldapi.domain.model.entity.UsersFollowersKey;
import org.example.realworldapi.domain.model.repository.UsersFollowersRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@ApplicationScoped
public class UsersFollowersRepositoryHibernate
    extends AbstractRepositoryHibernate<UsersFollowers, UsersFollowersKey>
    implements UsersFollowersRepository {

  private EntityManager entityManager;

  public UsersFollowersRepositoryHibernate(EntityManager entityManager) {
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

    ListJoin<User, Article> articles = follower.joinList("articles");

    criteriaQuery.select(articles);

    criteriaQuery.orderBy(builder.desc(articles.get("updatedAt")));

    return getPagedResultList(criteriaQuery, offset, limit);
  }

  @Override
  public int count(Long userId) {
    CriteriaBuilder builder = getCriteriaBuilder();

    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);

    Root<UsersFollowers> usersFollowers = getRoot(criteriaQuery, UsersFollowers.class);

    Join<UsersFollowers, User> user = usersFollowers.join("primaryKey").join("user");

    user.on(builder.equal(user.get("id"), userId));

    Join<UsersFollowers, User> follower = usersFollowers.join("primaryKey").join("follower");

    ListJoin<User, Article> articles = follower.joinList("articles");

    criteriaQuery.select(builder.count(articles));

    return getSingleResult(criteriaQuery).intValue();
  }
}
