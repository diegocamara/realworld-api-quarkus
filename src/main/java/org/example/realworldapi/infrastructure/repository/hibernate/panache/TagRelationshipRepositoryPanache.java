package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.TagRelationship;
import org.example.realworldapi.domain.model.article.TagRelationshipRepository;
import org.example.realworldapi.domain.model.tag.Tag;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.TagRelationshipEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.TagRelationshipEntityKey;

@ApplicationScoped
@AllArgsConstructor
public class TagRelationshipRepositoryPanache
    extends AbstractPanacheRepository<TagRelationshipEntity, TagRelationshipEntityKey>
    implements TagRelationshipRepository {

  private final EntityUtils entityUtils;

  @Override
  public void save(TagRelationship tagRelationship) {
    final var articleEntity = findArticleEntityById(tagRelationship.getArticle().getId());
    final var tagEntity = findTagEntityById(tagRelationship.getTag().getId());
    persist(new TagRelationshipEntity(articleEntity, tagEntity));
  }

  @Override
  public List<Tag> findArticleTags(Article article) {
    return find(
            "from TagRelationshipEntity as tagRelationship where tagRelationship.primaryKey.article.id = :articleId",
            Parameters.with("articleId", article.getId()))
        .list()
        .stream()
        .map(entityUtils::tag)
        .collect(Collectors.toList());
  }
}
