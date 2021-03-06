package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "NEWARTICLES_TAGS")
public class ArticlesTagsEntity {
  @EmbeddedId private ArticlesTagsEntityKey primaryKey;

  public ArticlesTagsEntity(ArticlesTagsEntityKey primaryKey) {
    this.primaryKey = primaryKey;
  }

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private TagEntity tag;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ArticlesTagsEntity that = (ArticlesTagsEntity) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
