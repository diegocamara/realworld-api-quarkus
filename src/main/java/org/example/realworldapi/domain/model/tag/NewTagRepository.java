package org.example.realworldapi.domain.model.tag;

import java.util.List;
import java.util.Optional;

public interface NewTagRepository {
  List<Tag> findAllTags();

  Optional<Tag> findByName(String name);

  void save(Tag tag);

  List<Tag> findByNames(List<String> names);
}
