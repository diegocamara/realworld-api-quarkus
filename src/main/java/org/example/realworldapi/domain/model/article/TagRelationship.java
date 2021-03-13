package org.example.realworldapi.domain.model.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.realworldapi.domain.model.tag.Tag;

@Data
@AllArgsConstructor
public class TagRelationship {
  private final Article article;
  private final Tag tag;
}
