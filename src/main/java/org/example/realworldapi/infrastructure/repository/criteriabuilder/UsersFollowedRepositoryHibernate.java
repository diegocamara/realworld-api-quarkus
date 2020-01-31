package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.UsersFollowed;
import org.example.realworldapi.domain.model.entity.UsersFollowedKey;
import org.example.realworldapi.domain.model.repository.UsersFollowedRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Deprecated
public class UsersFollowedRepositoryHibernate
    extends AbstractRepositoryHibernate<UsersFollowed, UsersFollowedKey>
    implements UsersFollowedRepository {

  private EntityManager entityManager;

  public UsersFollowedRepositoryHibernate(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<UsersFollowed> getEntityClass() {
    return UsersFollowed.class;
  }

  @Override
  public boolean isFollowing(Long currentUserId, Long followedUserId) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<UsersFollowed> usersFollowed = getRoot(criteriaQuery, UsersFollowed.class);
    criteriaQuery.select(builder.count(usersFollowed));
    criteriaQuery.where(
        builder.and(
            builder.equal(usersFollowed.get("primaryKey").get("user").get("id"), currentUserId),
            builder.equal(
                usersFollowed.get("primaryKey").get("followed").get("id"), followedUserId)));
    return getSingleResult(criteriaQuery).intValue() > 0;
  }

  @Override
  public UsersFollowed findByKey(UsersFollowedKey primaryKey) {
    return entityManager.find(UsersFollowed.class, primaryKey);
  }

  @Override
  public UsersFollowed create(UsersFollowed usersFollowed) {
    return persist(usersFollowed);
  }

  @Override
  public void remove(UsersFollowed usersFollowed) {
    entityManager.remove(usersFollowed);
  }
}
