package org.example.realworldapi.infrastructure.configuration;

import org.example.realworldapi.domain.feature.FindTags;
import org.example.realworldapi.domain.feature.impl.FindTagsImpl;
import org.example.realworldapi.domain.model.tag.NewTagRepository;
import org.example.realworldapi.domain.model.tag.TagBuilder;
import org.example.realworldapi.domain.validator.ModelValidator;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Dependent
public class TagsConfiguration {

  @Produces
  @Singleton
  public FindTags findTags(NewTagRepository tagRepository) {
    return new FindTagsImpl(tagRepository);
  }

  @Produces
  @Singleton
  public TagBuilder tagBuilder(ModelValidator modelValidator) {
    return new TagBuilder(modelValidator);
  }
}
