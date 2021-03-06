package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.repository.NewUserRepository;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.domain.model.user.UserModelBuilder;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
public class NewUserRepositoryPanache implements NewUserRepository, PanacheRepository<UserEntity> {

  private final UserModelBuilder userBuilder;

  @Override
  public void save(User user) {
    persist(new UserEntity(user));
  }

  @Override
  public boolean existsBy(String field, String value) {
    return count("upper(" + field + ")", value.toUpperCase().trim()) > 0;
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return find("upper(email)", email.toUpperCase().trim()).firstResultOptional().map(this::user);
  }

  private User user(UserEntity userEntity) {
    final var id = userEntity.getId();
    final var username = userEntity.getUsername();
    final var bio = userEntity.getBio();
    final var image = userEntity.getImage();
    final var password = userEntity.getPassword();
    final var email = userEntity.getEmail();
    return userBuilder.build(id, username, bio, image, password, email);
  }
}
