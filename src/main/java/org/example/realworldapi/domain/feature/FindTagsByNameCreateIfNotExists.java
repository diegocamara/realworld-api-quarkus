package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.tag.Tag;

import java.util.List;

public interface FindTagsByNameCreateIfNotExists {
  List<Tag> handle(List<String> names);
}
