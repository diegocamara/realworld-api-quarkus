package org.example.realworldapi.domain.entity.persistent;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ARTICLES_TAGS")
public class ArticlesTags {
  @EmbeddedId private ArticlesTagsKey primaryKey;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private Article article;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ArticlesTags that = (ArticlesTags) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
