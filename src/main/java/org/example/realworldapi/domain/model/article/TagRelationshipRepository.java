package org.example.realworldapi.domain.model.article;

import org.example.realworldapi.domain.model.tag.Tag;

import java.util.List;

public interface TagRelationshipRepository {
  void save(TagRelationship tagRelationship);

  List<Tag> findArticleTags(Article article);
}
