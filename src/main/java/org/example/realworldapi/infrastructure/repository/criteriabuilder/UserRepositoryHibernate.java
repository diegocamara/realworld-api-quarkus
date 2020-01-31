package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Optional;

@Deprecated
public class UserRepositoryHibernate extends AbstractRepositoryHibernate<User, Long>
    implements UserRepository {

  private EntityManager entityManager;

  public UserRepositoryHibernate(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public User create(User user) {
    return persist(user);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<User> criteriaQuery = getCriteriaQuery(builder);
    Root<User> user = getRoot(criteriaQuery);
    criteriaQuery
        .select(user)
        .where(builder.equal(builder.upper(user.get("email")), email.toUpperCase().trim()));
    return Optional.ofNullable(getSingleResult(criteriaQuery));
  }

  @Override
  public boolean existsBy(String field, String value) {
    CriteriaQuery<Long> criteriaQuery = existsByCriteriaQuery(field, null, value);
    return getSingleResult(criteriaQuery).intValue() > 0;
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return Optional.ofNullable(entityManager.find(User.class, id));
  }

  @Override
  public boolean existsUsername(Long excludeId, String username) {
    return existsBy("username", excludeId, username);
  }

  @Override
  public boolean existsEmail(Long excludeId, String email) {
    return existsBy("email", excludeId, email);
  }

  @Override
  public Optional<User> findByUsernameOptional(String username) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<User> criteriaQuery = getCriteriaQuery(builder);
    Root<User> user = getRoot(criteriaQuery);

    criteriaQuery.select(
        builder.construct(
            User.class, user.get("id"), user.get("username"), user.get("bio"), user.get("image")));

    criteriaQuery.where(
        builder.equal(builder.upper(user.get("username")), username.toUpperCase().trim()));

    return Optional.ofNullable(getSingleResult(criteriaQuery));
  }

  private boolean existsBy(String field, Long excludeId, String value) {
    CriteriaQuery<Long> criteriaQuery = existsByCriteriaQuery(field, excludeId, value);
    return getSingleResult(criteriaQuery).intValue() > 0;
  }

  private CriteriaQuery<Long> existsByCriteriaQuery(String field, Long excludeId, String value) {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
    Root<User> user = getRoot(criteriaQuery, User.class);

    Predicate notEqualIdExpression = builder.notEqual(user.get("id"), excludeId);
    Predicate equalFieldExpression =
        builder.equal(builder.upper(user.get(field)), value.toUpperCase().trim());

    Expression<Boolean> whereExpression =
        excludeId != null
            ? builder.and(notEqualIdExpression, equalFieldExpression)
            : equalFieldExpression;

    criteriaQuery.select(builder.count(user)).where(whereExpression);

    return criteriaQuery;
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<User> getEntityClass() {
    return User.class;
  }
}
