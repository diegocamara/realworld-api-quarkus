package org.example.realworldapi.infrastructure.repository.hibernate.panache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.ArticleEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.TagEntity;
import org.example.realworldapi.infrastructure.repository.hibernate.entity.UserEntity;

import java.util.UUID;

public class AbstractPanacheRepository<ENTITY, ID> implements PanacheRepositoryBase<ENTITY, ID> {

  protected UserEntity findUserEntityById(UUID id) {
    return getEntityManager().find(UserEntity.class, id);
  }

  protected TagEntity findTagEntityById(UUID id) {
    return getEntityManager().find(TagEntity.class, id);
  }

  protected ArticleEntity findArticleEntityById(UUID id) {
    return getEntityManager().find(ArticleEntity.class, id);
  }
}
