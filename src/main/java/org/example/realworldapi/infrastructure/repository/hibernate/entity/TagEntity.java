package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "NEWTAGS")
public class TagEntity {

  @Id private UUID id;

  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
  private List<ArticlesTagsEntity> articlesTags;

  public TagEntity(String name) {
    this.name = name;
  }
}
