package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.ArticlesTags;
import org.example.realworldapi.domain.model.entity.ArticlesTagsKey;
import org.example.realworldapi.domain.model.repository.ArticlesTagsRepository;

import javax.persistence.EntityManager;

@Deprecated
public class ArticlesTagsRepositoryHibernate
    extends AbstractRepositoryHibernate<ArticlesTags, ArticlesTagsKey>
    implements ArticlesTagsRepository {

  private EntityManager entityManager;

  public ArticlesTagsRepositoryHibernate(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<ArticlesTags> getEntityClass() {
    return ArticlesTags.class;
  }

  @Override
  public ArticlesTags create(ArticlesTags articlesTags) {
    return persist(articlesTags);
  }
}
