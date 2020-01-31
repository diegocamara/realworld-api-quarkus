package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

import static io.quarkus.panache.common.Parameters.with;

@ApplicationScoped
public class UserRepositoryPanache implements PanacheRepository<User>, UserRepository {

  @Override
  public User create(User user) {
    persistAndFlush(user);
    return user;
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return find("upper(email)", email.toUpperCase().trim()).firstResultOptional();
  }

  @Override
  public boolean existsBy(String field, String value) {
    return count("upper(" + field + ")", value.toUpperCase().trim()) > 0;
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return findByIdOptional(id);
  }

  @Override
  public boolean existsUsername(Long excludeId, String username) {
    return count(
            "id != :excludeId and upper(username) = :username",
            with("excludeId", excludeId).and("username", username.toUpperCase().trim()))
        > 0;
  }

  @Override
  public boolean existsEmail(Long excludeId, String email) {
    return count(
            "id != :excludeId and upper(email) = :email",
            with("excludeId", excludeId).and("email", email.toUpperCase().trim()))
        > 0;
  }

  @Override
  public Optional<User> findByUsernameOptional(String username) {
    return find("upper(username)", username.toUpperCase().trim()).firstResultOptional();
  }
}
