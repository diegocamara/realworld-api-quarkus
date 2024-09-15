package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TAG_RELATIONSHIP")
public class TagRelationshipEntity {
  @EmbeddedId private TagRelationshipEntityKey primaryKey;

  public TagRelationshipEntity(TagRelationshipEntityKey primaryKey) {
    this.primaryKey = primaryKey;
  }

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(insertable = false, updatable = false)
  private TagEntity tag;

  public TagRelationshipEntity(ArticleEntity article, TagEntity tag) {
    final var tagRelationshipEntityKey = new TagRelationshipEntityKey();
    tagRelationshipEntityKey.setArticle(article);
    tagRelationshipEntityKey.setTag(tag);
    this.primaryKey = tagRelationshipEntityKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    TagRelationshipEntity that = (TagRelationshipEntity) o;
    return Objects.equals(primaryKey, that.primaryKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryKey);
  }
}
