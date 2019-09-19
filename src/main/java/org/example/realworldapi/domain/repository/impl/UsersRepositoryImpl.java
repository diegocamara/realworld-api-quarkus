package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.UsersRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@ApplicationScoped
public class UsersRepositoryImpl extends AbstractRepository<User, Long> implements UsersRepository {

    private EntityManager entityManager;

    public UsersRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> create(User user) {
        entityManager.persist(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(builder);
        Root<User> user = getRoot(criteriaQuery);
        criteriaQuery.select(user).where(builder.equal(builder.upper(user.get("email")), email.toUpperCase().trim()));
        return Optional.ofNullable(getSingleResult(criteriaQuery));
    }

    @Override
    public boolean exists(String email) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
        Root<User> user = getRoot(criteriaQuery, User.class);
        criteriaQuery.select(builder.count(user))
                .where(builder.equal(builder.upper(user.get("email")), email.toUpperCase().trim()));
        return getSingleResult(criteriaQuery).intValue() > 0;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    public boolean existsUsername(Long excludeId, String username) {
        return existsBy("username", excludeId, username);
    }

    @Override
    public boolean existsEmail(Long excludeId, String email) {
        return existsBy("email", excludeId, email);
    }

    private Optional<User> findBy(String field, Object value){
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(builder);
        Root<User> user = getRoot(criteriaQuery);
        criteriaQuery.select(user).where(builder.equal(user.get(field), value));
        return Optional.ofNullable(getSingleResult(criteriaQuery));
    }

    private boolean existsBy(String field, Long excludeId, String value){
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = getCriteriaQuery(builder, Long.class);
        Root<User> user = getRoot(criteriaQuery, User.class);
        criteriaQuery.select(builder.count(user))
                .where(builder.and(builder.notEqual(user.get("id"), excludeId), builder.equal(builder.upper(user.get(field)), value.toUpperCase().trim())));
        return getSingleResult(criteriaQuery).intValue() > 0;
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
