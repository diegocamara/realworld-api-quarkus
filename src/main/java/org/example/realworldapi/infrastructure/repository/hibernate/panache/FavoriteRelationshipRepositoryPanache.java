package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.FavoriteRelationshipRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.FavoriteRelationshipEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.FavoriteRelationshipEntityKey;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

import static io.quarkus.panache.common.Parameters.with;

@ApplicationScoped
public class FavoriteRelationshipRepositoryPanache
    extends AbstractPanacheRepository<FavoriteRelationshipEntity, FavoriteRelationshipEntityKey>
    implements FavoriteRelationshipRepository {

  @Override
  public boolean isFavorited(Article article, UUID currentUserId) {
    return count(
            "article.id = :articleId and user.id = :currentUserId",
            with("articleId", article.getId()).and("currentUserId", currentUserId))
        > 0;
  }

  @Override
  public long favoritesCount(Article article) {
    return count("article.id", article.getId());
  }
}
