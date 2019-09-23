package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.repository.ArticleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

@ApplicationScoped
public class ArticleRepositoryImpl extends AbstractRepository<Article, Long>
    implements ArticleRepository {

  private EntityManager entityManager;

  public ArticleRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<Article> getEntityClass() {
    return Article.class;
  }
}
