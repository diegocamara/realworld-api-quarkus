package org.example.realworldapi.domain.feature;

import org.example.realworldapi.domain.model.tag.Tag;

public interface CreateTag {
  Tag handle(String name);
}
