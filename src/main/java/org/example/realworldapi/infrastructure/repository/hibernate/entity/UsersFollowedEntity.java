package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "FOLLOWED_USERS")
public class UsersFollowedEntity {
  @EmbeddedId private UsersFollowedEntityKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private UserEntity followed;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    UsersFollowedEntity that = (UsersFollowedEntity) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
