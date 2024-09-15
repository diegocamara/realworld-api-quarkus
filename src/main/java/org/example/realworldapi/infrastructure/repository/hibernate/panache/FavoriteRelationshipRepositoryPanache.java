package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import static io.quarkus.panache.common.Parameters.with;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.FavoriteRelationship;
import org.example.realworldapi.domain.model.article.FavoriteRelationshipRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.FavoriteRelationshipEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.FavoriteRelationshipEntityKey;

@ApplicationScoped
@AllArgsConstructor
public class FavoriteRelationshipRepositoryPanache
    extends AbstractPanacheRepository<FavoriteRelationshipEntity, FavoriteRelationshipEntityKey>
    implements FavoriteRelationshipRepository {

  private final EntityUtils entityUtils;

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

  @Override
  public Optional<FavoriteRelationship> findByArticleIdAndUserId(
      UUID articleId, UUID currentUserId) {
    return find(
            "article.id = :articleId and user.id = :currentUserId",
            with("articleId", articleId).and("currentUserId", currentUserId))
        .firstResultOptional()
        .map(entityUtils::favoriteRelationship);
  }

  @Override
  public void save(FavoriteRelationship favoriteRelationship) {
    final var userEntity = findUserEntityById(favoriteRelationship.getUser().getId());
    final var articleEntity = findArticleEntityById(favoriteRelationship.getArticle().getId());
    persist(new FavoriteRelationshipEntity(userEntity, articleEntity));
  }

  @Override
  public void delete(FavoriteRelationship favoriteRelationship) {
    final var favoriteRelationshipEntity =
        findFavoriteRelationshipEntityByKey(favoriteRelationship);
    delete(favoriteRelationshipEntity);
  }
}
