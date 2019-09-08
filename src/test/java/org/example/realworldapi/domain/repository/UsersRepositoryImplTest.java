package org.example.realworldapi.domain.repository;

import io.quarkus.test.junit.QuarkusTest;
import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.repository.impl.UsersRepositoryImpl;
import org.example.realworldapi.util.UserUtils;
import org.example.realworldapi.DatabaseCleanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Optional;

@QuarkusTest
public class UsersRepositoryImplTest {

    EntityManager entityManager;
    DataSource dataSource;
    private DatabaseCleanner databaseCleanner;
    private UsersRepository usersRepository;

    public UsersRepositoryImplTest(EntityManager entityManager, DataSource dataSource) throws SQLException {
        this.entityManager = entityManager;
        this.dataSource = dataSource;
        this.databaseCleanner = new DatabaseCleanner(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        usersRepository = new UsersRepositoryImpl(entityManager);
    }

    @AfterEach
    public void afterEach(){
        this.databaseCleanner.clear();
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
