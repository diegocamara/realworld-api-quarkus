package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "FAVORITE_RELATIONSHIP")
public class FavoriteRelationshipEntity {

  @EmbeddedId private FavoriteRelationshipEntityKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private UserEntity user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    FavoriteRelationshipEntity that = (FavoriteRelationshipEntity) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
