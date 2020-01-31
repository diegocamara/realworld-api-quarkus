package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "FOLLOWED_USERS")
public class UsersFollowed {
  @EmbeddedId private UsersFollowedKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private User user;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private User followed;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    UsersFollowed that = (UsersFollowed) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
