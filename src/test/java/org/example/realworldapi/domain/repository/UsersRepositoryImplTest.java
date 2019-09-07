package org.example.realworldapi.domain.repository;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.impl.UsersRepositoryImpl;
import org.example.realworldapi.util.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

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

        User user = UserUtils.create("user", "user@mail.com", "123");

        User createdUser = usersRepository.create(user);

        User persistedUser = entityManager.find(User.class, createdUser.getId());

        Assertions.assertNotNull(persistedUser);

    }

    @Test
    @Transactional
    public void shouldReturnAUserByEmail(){

        User existingUser = UserUtils.create("user1", "user@mail.com", "123");

        entityManager.persist(existingUser);

        Optional<User> result = usersRepository.findByEmail(existingUser.getEmail());

        Assertions.assertEquals(existingUser.getEmail(), result.orElse(new User()).getEmail());

    }

}
