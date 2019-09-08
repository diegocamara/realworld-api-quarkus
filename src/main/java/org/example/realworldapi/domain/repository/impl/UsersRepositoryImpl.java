package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.UsersRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.util.Optional;

@ApplicationScoped
public class UsersRepositoryImpl extends AbstractRepository<User, Long> implements UsersRepository {

    private EntityManager entityManager;

    public UsersRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        getEntityManager().persist(user);
        return user;
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
    EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    Class<User> getEntityClass() {
        return User.class;
    }
}
