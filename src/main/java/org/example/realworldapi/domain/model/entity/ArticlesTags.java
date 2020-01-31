package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ARTICLES_TAGS")
public class ArticlesTags {
  @EmbeddedId private ArticlesTagsKey primaryKey;

  public ArticlesTags(ArticlesTagsKey primaryKey) {
    this.primaryKey = primaryKey;
  }

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private Article article;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private Tag tag;

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
