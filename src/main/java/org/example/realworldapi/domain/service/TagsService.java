package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.model.entity.Tag;

import java.util.List;

public interface TagsService {
  List<Tag> findTags();
}
