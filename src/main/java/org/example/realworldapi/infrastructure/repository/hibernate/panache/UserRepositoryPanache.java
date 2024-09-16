package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import static io.quarkus.panache.common.Parameters.with;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.UserEntity;

@ApplicationScoped
@AllArgsConstructor
public class UserRepositoryPanache extends AbstractPanacheRepository<UserEntity, UUID>
    implements UserRepository {

  private final EntityUtils entityUtils;

  @Override
  public void save(User user) {
    persist(new UserEntity(user));
  }

  @Override
  public boolean existsBy(String field, String value) {
    return count("upper(" + field + ")", value.toUpperCase().trim()) > 0;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return find("upper(email)", email.toUpperCase().trim())
        .firstResultOptional()
        .map(entityUtils::user);
  }

  @Override
  public Optional<User> findUserById(UUID id) {
    return findByIdOptional(id).map(entityUtils::user);
  }

  @Override
  public boolean existsUsername(UUID excludeId, String username) {
    return count(
            "id != :excludeId and upper(username) = :username",
            with("excludeId", excludeId).and("username", username.toUpperCase().trim()))
        > 0;
  }

  @Override
  public boolean existsEmail(UUID excludeId, String email) {
    return count(
            "id != :excludeId and upper(email) = :email",
            with("excludeId", excludeId).and("email", email.toUpperCase().trim()))
        > 0;
  }

  @Override
  public void update(User user) {
    final var userEntity = findById(user.getId());
    userEntity.update(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return find("upper(username)", username.toUpperCase().trim())
        .firstResultOptional()
        .map(entityUtils::user);
  }
}
