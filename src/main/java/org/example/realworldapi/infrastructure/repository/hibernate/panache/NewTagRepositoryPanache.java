package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.tag.NewTagRepository;
import org.example.realworldapi.domain.model.tag.Tag;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.TagEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@AllArgsConstructor
public class NewTagRepositoryPanache
    implements NewTagRepository, PanacheRepositoryBase<TagEntity, UUID> {

  private final EntityUtils entityUtils;

  @Override
  public List<Tag> findAllTags() {
    return listAll().stream().map(entityUtils::tag).collect(Collectors.toList());
  }
}
