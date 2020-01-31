package org.example.realworldapi.application;

import org.example.realworldapi.domain.model.entity.Tag;
import org.example.realworldapi.domain.model.repository.TagRepository;
import org.example.realworldapi.domain.service.TagsService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TagsServiceImpl implements TagsService {

  private TagRepository tagRepository;

  public TagsServiceImpl(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  @Override
  @Transactional
  public List<Tag> findTags() {
    return tagRepository.findAllTags();
  }
}
