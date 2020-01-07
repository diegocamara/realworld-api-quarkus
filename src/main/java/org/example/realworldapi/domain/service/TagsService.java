package org.example.realworldapi.domain.service;

import org.example.realworldapi.domain.model.entity.persistent.Tag;

import java.util.List;

public interface TagsService {
  List<Tag> findTags();
}
