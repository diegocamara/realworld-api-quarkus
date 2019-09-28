package org.example.realworldapi.domain.repository.impl;

import org.example.realworldapi.domain.entity.persistent.Tag;
import org.example.realworldapi.domain.repository.TagRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@ApplicationScoped
public class TagRepositoryImpl extends AbstractRepository<Tag, Long> implements TagRepository {

  private EntityManager entityManager;

  public TagRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Tag> findByName(String tagName) {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = getCriteriaQuery(builder);
    Root<Tag> tag = getRoot(criteriaQuery);

    criteriaQuery.select(tag);
    criteriaQuery.where(
        builder.equal(builder.upper(tag.get("name")), tagName.toUpperCase().trim()));

    return Optional.ofNullable(getSingleResult(criteriaQuery));
  }

  @Override
  public Tag create(Tag tag) {
    entityManager.persist(tag);
    return tag;
  }

  @Override
  EntityManager getEntityManager() {
    return this.entityManager;
  }

  @Override
  Class<Tag> getEntityClass() {
    return Tag.class;
  }
}
