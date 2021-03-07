package org.example.realworldapi.domain.feature.impl;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.feature.FindTags;
import org.example.realworldapi.domain.model.tag.NewTagRepository;
import org.example.realworldapi.domain.model.tag.Tag;

import java.util.List;

@AllArgsConstructor
public class FindTagsImpl implements FindTags {

  private final NewTagRepository tagRepository;

  @Override
  public List<Tag> handle() {
    return tagRepository.findAllTags();
  }
}
