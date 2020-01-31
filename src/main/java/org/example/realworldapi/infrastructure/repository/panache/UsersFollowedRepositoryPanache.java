package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import org.example.realworldapi.domain.model.entity.UsersFollowed;
import org.example.realworldapi.domain.model.entity.UsersFollowedKey;
import org.example.realworldapi.domain.model.repository.UsersFollowedRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersFollowedRepositoryPanache
    implements PanacheRepository<UsersFollowed>, UsersFollowedRepository {

  @Override
  public boolean isFollowing(Long currentUserId, Long followedUserId) {
    return count(
            "primaryKey.user.id = :currentUserId and primaryKey.followed.id = :followedUserId",
            Parameters.with("currentUserId", currentUserId).and("followedUserId", followedUserId))
        > 0;
  }

  @Override
  public UsersFollowed findByKey(UsersFollowedKey primaryKey) {
    return find("primaryKey", primaryKey).firstResult();
  }

  @Override
  public UsersFollowed create(UsersFollowed usersFollowed) {
    persistAndFlush(usersFollowed);
    return usersFollowed;
  }

  @Override
  public void remove(UsersFollowed usersFollowed) {
    delete(usersFollowed);
  }
}
