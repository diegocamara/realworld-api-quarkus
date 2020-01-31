package org.example.realworldapi.infrastructure.repository.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.example.realworldapi.domain.model.entity.ArticlesTags;
import org.example.realworldapi.domain.model.repository.ArticlesTagsRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArticlesTagsRepositoryPanache
    implements PanacheRepository<ArticlesTags>, ArticlesTagsRepository {

  @Override
  public ArticlesTags create(ArticlesTags articlesTags) {
    persistAndFlush(articlesTags);
    return articlesTags;
  }
}
