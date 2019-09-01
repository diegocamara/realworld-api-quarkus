package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.UsersRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersRepositoryImpl implements UsersRepository {

    @Override
    public User create(User user) {
        return null;
    }
}
