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
@Table(name = "FAVORITE_RELATIONSHIP")
public class FavoriteRelationshipEntity {

  @EmbeddedId private FavoriteRelationshipEntityKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private UserEntity user;

  public FavoriteRelationshipEntity(UserEntity user, ArticleEntity article) {
    final var favoriteRelationshipEntityKey = new FavoriteRelationshipEntityKey();
    favoriteRelationshipEntityKey.setUser(user);
    favoriteRelationshipEntityKey.setArticle(article);
    this.primaryKey = favoriteRelationshipEntityKey;
  }

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
