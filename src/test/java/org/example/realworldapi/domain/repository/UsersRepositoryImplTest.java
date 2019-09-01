package org.example.realworldapi.domain.repository;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class UsersRepositoryImplTest {

    @Inject
    EntityManager entityManager;

    @Test
    public void testing() {
        entityManager.clear();
    }

}
