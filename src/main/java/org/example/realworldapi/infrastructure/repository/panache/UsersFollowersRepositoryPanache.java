package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import org.example.realworldapi.domain.model.entity.UsersFollowers;
import org.example.realworldapi.domain.model.entity.UsersFollowersKey;
import org.example.realworldapi.domain.model.repository.UsersFollowersRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersFollowersRepositoryPanache
    implements PanacheRepository<UsersFollowers>, UsersFollowersRepository {

  @Override
  public boolean isFollowing(Long currentUserId, Long followerUserId) {
    return count(
            "primaryKey.user.id = :currentUserId and primaryKey.follower.id = :followerUserId",
            Parameters.with("currentUserId", currentUserId).and("followerUserId", followerUserId))
        > 0;
  }

  @Override
  public UsersFollowers findByKey(UsersFollowersKey primaryKey) {
    return find("primaryKey", primaryKey).firstResult();
  }

  @Override
  public UsersFollowers create(UsersFollowers usersFollowers) {
    persistAndFlush(usersFollowers);
    return usersFollowers;
  }

  @Override
  public void remove(UsersFollowers usersFollowers) {
    delete(usersFollowers);
  }
}
