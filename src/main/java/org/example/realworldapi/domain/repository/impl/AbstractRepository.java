package org.example.realworldapi.domain.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public abstract class AbstractRepository<T, ID> {

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

    protected Session getHibernateSession(){
        return getEntityManager().unwrap(Session.class);
    }

    public T getEntityProxy(Serializable id) {
        return getHibernateSession().load(getEntityClass(), id);
    }

    abstract EntityManager getEntityManager();

    abstract Class<T> getEntityClass();

}
