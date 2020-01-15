package org.example.realworldapi.domain.model.repository;

import org.example.realworldapi.domain.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
  Optional<Tag> findByName(String tagName);

  Tag create(Tag tag);

  List<Tag> findAllTags();

  List<Tag> findArticleTags(Long articleId);
}
