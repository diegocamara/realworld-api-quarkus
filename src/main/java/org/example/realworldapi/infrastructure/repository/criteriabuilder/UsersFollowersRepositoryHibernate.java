package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.UsersFollowers;
import org.example.realworldapi.domain.model.entity.UsersFollowersKey;
import org.example.realworldapi.domain.model.repository.UsersFollowersRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

// @ApplicationScoped
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
  public UsersFollowers create(UsersFollowers usersFollowers) {
    return persist(usersFollowers);
  }

  @Override
  public void remove(UsersFollowers usersFollowers) {
    entityManager.remove(usersFollowers);
  }
}
