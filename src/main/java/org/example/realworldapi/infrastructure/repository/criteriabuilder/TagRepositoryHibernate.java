package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.example.realworldapi.domain.model.entity.ArticlesTags;
import org.example.realworldapi.domain.model.entity.Tag;
import org.example.realworldapi.domain.model.repository.TagRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Deprecated
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
  public List<Tag> findAllTags() {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = getCriteriaQuery(builder);
    Root<Tag> tags = getRoot(criteriaQuery);
    criteriaQuery.select(tags);
    return getResultList(criteriaQuery);
  }

  @Override
  public List<Tag> findArticleTags(Long articleId) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<Tag> criteriaQuery = getCriteriaQuery(builder);
    Root<Tag> tags = getRoot(criteriaQuery);
    ListJoin<Tag, ArticlesTags> articlesTags = tags.joinList("articlesTags");
    criteriaQuery.select(tags);
    criteriaQuery.where(builder.equal(articlesTags.get("article").get("id"), articleId));
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
