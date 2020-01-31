package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ArticlesUsersKey implements Serializable {

  @ManyToOne private Article article;
  @ManyToOne private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ArticlesUsersKey that = (ArticlesUsersKey) o;
    return Objects.equals(user, that.user) && Objects.equals(article, that.article);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, article);
  }
}
