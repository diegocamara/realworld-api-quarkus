package org.example.realworldapi.domain.repository;

import org.example.realworldapi.domain.entity.User;

public interface UsersRepository {
    User create(User user);
}
