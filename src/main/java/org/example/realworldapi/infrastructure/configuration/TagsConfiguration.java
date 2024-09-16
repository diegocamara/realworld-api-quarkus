package org.example.realworldapi.infrastructure.configuration;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.example.realworldapi.domain.feature.CreateTag;
import org.example.realworldapi.domain.feature.FindTags;
import org.example.realworldapi.domain.feature.FindTagsByNameCreateIfNotExists;
import org.example.realworldapi.domain.feature.impl.CreateTagImpl;
import org.example.realworldapi.domain.feature.impl.FindTagsByNameCreateIfNotExistsImpl;
import org.example.realworldapi.domain.feature.impl.FindTagsImpl;
import org.example.realworldapi.domain.model.tag.TagBuilder;
import org.example.realworldapi.domain.model.tag.TagRepository;
import org.example.realworldapi.domain.validator.ModelValidator;

@Dependent
public class TagsConfiguration {

  @Produces
  @Singleton
  public FindTags findTags(TagRepository tagRepository) {
    return new FindTagsImpl(tagRepository);
  }

  @Produces
  @Singleton
  public CreateTag createTag(TagRepository tagRepository, TagBuilder tagBuilder) {
    return new CreateTagImpl(tagRepository, tagBuilder);
  }

  @Produces
  @Singleton
  public FindTagsByNameCreateIfNotExists findTagsByNameCreateIfNotExists(
      TagRepository tagRepository, CreateTag createTag) {
    return new FindTagsByNameCreateIfNotExistsImpl(tagRepository, createTag);
  }

  @Produces
  @Singleton
  public TagBuilder tagBuilder(ModelValidator modelValidator) {
    return new TagBuilder(modelValidator);
  }
}
