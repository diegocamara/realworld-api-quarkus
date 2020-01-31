package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.example.realworldapi.domain.model.entity.ArticlesUsers;
import org.example.realworldapi.domain.model.entity.ArticlesUsersKey;
import org.example.realworldapi.domain.model.repository.ArticlesUsersRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

import static io.quarkus.panache.common.Parameters.with;

@ApplicationScoped
public class ArticlesUsersRepositoryPanache
    implements PanacheRepository<ArticlesUsers>, ArticlesUsersRepository {

  @Override
  public boolean isFavorited(Long articleId, Long currentUserId) {
    return count(
            "article.id = :articleId and user.id = :currentUserId",
            with("articleId", articleId).and("currentUserId", currentUserId))
        > 0;
  }

  @Override
  public long favoritesCount(Long articleId) {
    return count("article.id", articleId);
  }

  @Override
  public ArticlesUsers create(ArticlesUsers articlesUsers) {
    persistAndFlush(articlesUsers);
    return articlesUsers;
  }

  @Override
  public Optional<ArticlesUsers> findById(ArticlesUsersKey articlesUsersKey) {
    return find("primaryKey", articlesUsersKey).firstResultOptional();
  }

  @Override
  public void remove(ArticlesUsers articlesUsers) {
    delete(articlesUsers);
  }
}
