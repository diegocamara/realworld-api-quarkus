package org.example.realworldapi.web.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.entity.persistent.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RegisterForReflection
public class TagsDTO {

  private List<String> tags;

  public TagsDTO(List<Tag> tags) {
    this.tags = tags.stream().map(Tag::getName).collect(Collectors.toList());
  }
}
