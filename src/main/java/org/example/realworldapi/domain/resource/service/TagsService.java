package org.example.realworldapi.domain.resource.service;

import org.example.realworldapi.domain.entity.persistent.Tag;

import java.util.List;

public interface TagsService {
  List<Tag> findTags();
}
