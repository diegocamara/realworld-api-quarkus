package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class FollowRelationshipEntityKey implements Serializable {

  @ManyToOne private UserEntity user;

  @ManyToOne private UserEntity followed;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    FollowRelationshipEntityKey that = (FollowRelationshipEntityKey) o;
    return Objects.equals(user, that.user) && Objects.equals(followed, that.followed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, followed);
  }
}
