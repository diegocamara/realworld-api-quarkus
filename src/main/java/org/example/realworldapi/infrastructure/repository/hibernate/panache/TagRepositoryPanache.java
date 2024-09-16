package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.tag.Tag;
import org.example.realworldapi.domain.model.tag.TagRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.TagEntity;

@ApplicationScoped
@AllArgsConstructor
public class TagRepositoryPanache extends AbstractPanacheRepository<TagEntity, UUID>
    implements TagRepository {

  private final EntityUtils entityUtils;

  @Override
  public List<Tag> findAllTags() {
    return listAll().stream().map(entityUtils::tag).collect(Collectors.toList());
  }

  @Override
  public Optional<Tag> findByName(String name) {
    return find("upper(name)", name.toUpperCase().trim())
        .firstResultOptional()
        .map(entityUtils::tag);
  }

  @Override
  public void save(Tag tag) {
    persist(new TagEntity(tag));
  }

  @Override
  public List<Tag> findByNames(List<String> names) {
    final var tagsEntity =
        find(
                "select tags from TagEntity as tags where upper(tags.name) in (:names)",
                Parameters.with("names", toUpperCase(names)))
            .list();
    return tagsEntity.stream().map(entityUtils::tag).collect(Collectors.toList());
  }
}
