package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.user.FollowRelationship;
import org.example.realworldapi.domain.model.user.FollowRelationshipRepository;
import org.example.realworldapi.domain.model.user.User;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.FollowRelationshipEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.FollowRelationshipEntityKey;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.UserEntity;

@ApplicationScoped
@AllArgsConstructor
public class FollowRelationshipRepositoryPanache
    extends AbstractPanacheRepository<FollowRelationshipEntity, FollowRelationshipEntityKey>
    implements FollowRelationshipRepository {

  private final EntityUtils entityUtils;

  @Override
  public boolean isFollowing(UUID currentUserId, UUID followedUserId) {
    return count(
            "primaryKey.user.id = :currentUserId and primaryKey.followed.id = :followedUserId",
            Parameters.with("currentUserId", currentUserId).and("followedUserId", followedUserId))
        > 0;
  }

  @Override
  public void save(FollowRelationship followRelationship) {
    final var userEntity = findUserEntityById(followRelationship.getUser().getId());
    final var userToFollowEntity = findUserEntityById(followRelationship.getFollowed().getId());
    persistAndFlush(new FollowRelationshipEntity(userEntity, userToFollowEntity));
  }

  @Override
  public Optional<FollowRelationship> findByUsers(User loggedUser, User followedUser) {
    return findUsersFollowedEntityByUsers(loggedUser, followedUser)
        .map(this::followingRelationship);
  }

  @Override
  public void remove(FollowRelationship followRelationship) {
    final var usersFollowedEntity =
        findUsersFollowedEntityByUsers(
                followRelationship.getUser(), followRelationship.getFollowed())
            .orElseThrow();
    delete(usersFollowedEntity);
  }

  private Optional<FollowRelationshipEntity> findUsersFollowedEntityByUsers(
      User loggedUser, User followedUser) {
    final var loggedUserEntity = findUserEntityById(loggedUser.getId());
    final var followedEntity = findUserEntityById(followedUser.getId());
    return find("primaryKey", usersFollowedKey(loggedUserEntity, followedEntity))
        .firstResultOptional();
  }

  private FollowRelationship followingRelationship(
      FollowRelationshipEntity followRelationshipEntity) {
    final var user = entityUtils.user(followRelationshipEntity.getUser());
    final var followed = entityUtils.user(followRelationshipEntity.getFollowed());
    return new FollowRelationship(user, followed);
  }

  private FollowRelationshipEntityKey usersFollowedKey(UserEntity user, UserEntity followed) {
    final var primaryKey = new FollowRelationshipEntityKey();
    primaryKey.setUser(user);
    primaryKey.setFollowed(followed);
    return primaryKey;
  }
}
