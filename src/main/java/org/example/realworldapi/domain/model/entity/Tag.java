package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "TAGS")
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
  private List<ArticlesTags> articlesTags;

  public Tag(String name) {
    this.name = name;
  }
}
