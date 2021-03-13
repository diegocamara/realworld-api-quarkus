package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class FavoriteRelationshipEntityKey implements Serializable {

  @ManyToOne private ArticleEntity article;
  @ManyToOne private UserEntity user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    FavoriteRelationshipEntityKey that = (FavoriteRelationshipEntityKey) o;
    return Objects.equals(user, that.user) && Objects.equals(article, that.article);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, article);
  }
}
