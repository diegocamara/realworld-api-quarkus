package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "FOLLOW_RELATIONSHIP")
public class FollowRelationshipEntity {
  @EmbeddedId private FollowRelationshipEntityKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private UserEntity followed;

  public FollowRelationshipEntity(UserEntity user, UserEntity followed) {
    final var usersFollowedEntityKey = new FollowRelationshipEntityKey();
    usersFollowedEntityKey.setUser(user);
    usersFollowedEntityKey.setFollowed(followed);
    this.primaryKey = usersFollowedEntityKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    FollowRelationshipEntity that = (FollowRelationshipEntity) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
