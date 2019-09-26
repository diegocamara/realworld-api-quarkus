package org.example.realworldapi.domain.entity.persistent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ARTICLES")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String slug;
  private String title;
  private String description;
  private String body;
  @CreationTimestamp private LocalDateTime createdAt;
  @UpdateTimestamp private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
  private List<ArticlesTags> tags;

  @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
  private List<ArticlesUsers> favorites;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Article that = (Article) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
