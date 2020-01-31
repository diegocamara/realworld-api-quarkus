package org.example.realworldapi.infrastructure.repository.criteriabuilder;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Deprecated
public abstract class AbstractRepositoryHibernate<T, ID> {

  protected CriteriaBuilder getCriteriaBuilder() {
    return getHibernateSession().getCriteriaBuilder();
  }

  protected CriteriaQuery<T> getCriteriaQuery(CriteriaBuilder criteriaBuilder) {
    return criteriaBuilder.createQuery(getEntityClass());
  }

  protected <E> CriteriaQuery<E> getCriteriaQuery(CriteriaBuilder criteriaBuilder, Class<E> clazz) {
    return criteriaBuilder.createQuery(clazz);
  }

  protected <E> Root<E> getRoot(CriteriaQuery<?> criteriaQuery, Class<E> clazz) {
    return criteriaQuery.from(clazz);
  }

  protected Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
    return criteriaQuery.from(getEntityClass());
  }

  protected <E> E getSingleResult(CriteriaQuery<E> criteriaQuery) {
    Query<E> query = getHibernateSession().createQuery(criteriaQuery);
    try {
      return query.getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  protected <E> List<E> getResultList(CriteriaQuery<E> criteriaQuery) {
    TypedQuery<E> query = getHibernateSession().createQuery(criteriaQuery);
    try {
      return query.getResultList();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  protected <E> List<E> getPagedResultList(CriteriaQuery<E> criteriaQuery, int offset, int limit) {
    TypedQuery<E> query =
        getHibernateSession()
            .createQuery(criteriaQuery)
            .setFirstResult(offset)
            .setMaxResults(limit);
    try {
      return query.getResultList();
    } catch (NoResultException noResultException) {
      return null;
    }
  }

  protected T persist(T object) {
    getEntityManager().persist(object);
    getEntityManager().flush();
    return object;
  }

  protected Session getHibernateSession() {
    return getEntityManager().unwrap(Session.class);
  }

  public T getEntityProxy(Serializable id) {
    return getHibernateSession().load(getEntityClass(), id);
  }

  abstract EntityManager getEntityManager();

  abstract Class<T> getEntityClass();
}
