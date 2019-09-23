package org.example.realworldapi.domain.entity.persistent;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UsersFollowersKey implements Serializable {

  @ManyToOne private User user;

  @ManyToOne private User follower;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    UsersFollowersKey that = (UsersFollowersKey) o;
    return Objects.equals(user, that.user) && Objects.equals(follower, that.follower);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, follower);
  }
}
