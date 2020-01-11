package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ARTICLES_USERS")
public class ArticlesUsers {

  @EmbeddedId private ArticlesUsersKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private Article article;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ArticlesUsers that = (ArticlesUsers) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
