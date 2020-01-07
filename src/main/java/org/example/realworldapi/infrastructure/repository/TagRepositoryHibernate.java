package org.example.realworldapi.infrastructure.repository;

import org.example.realworldapi.domain.model.entity.persistent.Tag;
import org.example.realworldapi.domain.model.repository.TagRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TagRepositoryHibernate extends AbstractRepositoryHibernate<Tag, Long>
    implements TagRepository {

  private EntityManager entityManager;

  public TagRepositoryHibernate(EntityManager entityManager) {
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
    return persist(tag);
  }

  @Override
  public List<Tag> findAll() {

    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = getCriteriaQuery(builder);
    Root<Tag> tag = getRoot(criteriaQuery);

    criteriaQuery.select(tag);

    return getResultList(criteriaQuery);
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
