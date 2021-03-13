package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import lombok.AllArgsConstructor;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.article.NewArticleRepository;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.ArticleEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.EntityUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@AllArgsConstructor
public class NewArticleRepositoryPanache extends AbstractPanacheRepository<ArticleEntity, UUID>
    implements NewArticleRepository {

  private final EntityUtils entityUtils;

  @Override
  public boolean existsBySlug(String slug) {
    return count("upper(slug)", slug.toUpperCase().trim()) > 0;
  }

  @Override
  public void save(Article article) {
    final var author = findUserEntityById(article.getAuthor().getId());
    persistAndFlush(new ArticleEntity(article, author));
  }

  @Override
  public Optional<Article> findArticleById(UUID id) {
    return findByIdOptional(id).map(entityUtils::article);
  }
}
