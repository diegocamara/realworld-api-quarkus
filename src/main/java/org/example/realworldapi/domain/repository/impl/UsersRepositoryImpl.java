package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.UsersRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

@ApplicationScoped
public class UsersRepositoryImpl implements UsersRepository {

    private EntityManager entityManager;

    public UsersRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }
}
