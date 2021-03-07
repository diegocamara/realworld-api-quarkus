package org.example.realworldapi.domain.model.tag;

import java.util.List;

public interface NewTagRepository {
  List<Tag> findAllTags();
}
