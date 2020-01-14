package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "USERS_FOLLOWERS")
public class UsersFollowers {
  @EmbeddedId private UsersFollowersKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private User user;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private User follower;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    UsersFollowers that = (UsersFollowers) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
