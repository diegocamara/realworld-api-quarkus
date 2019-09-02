package org.example.realworldapi.domain.repository;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.impl.UsersRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.UUID;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class UsersRepositoryImplTest {

    @Inject
    EntityManager entityManager;

    private UsersRepository usersRepository;

    @BeforeEach
    public void beforeEach() {
        usersRepository = new UsersRepositoryImpl(entityManager);
    }

    @Test
    @Transactional
    public void shouldCreateAnUser() {

        User user = new User();
        user.setUsername("User");
        user.setEmail("user@mail.com");
        user.setPassword(BCrypt.hashpw("user123", BCrypt.gensalt()));
        user.setToken(UUID.randomUUID().toString());

        User createdUser = usersRepository.create(user);

        User persistedUser = entityManager.find(User.class, createdUser.getId());

        Assertions.assertNotNull(persistedUser);

    }
}
