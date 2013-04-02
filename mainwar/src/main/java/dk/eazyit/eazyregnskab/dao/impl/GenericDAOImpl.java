package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.GenericDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * JPA implementation of the GenericRepository. Note that this implementation
 * also expects Hibernate as JPA implementation. That's because we like the
 * Criteria API.
 * <p/>
 * Based on the work by <a href="http://www.bejug.org/confluenceBeJUG/display/BeJUG/Generic+DAO+example">Jurgen Lust</a>.
 *
 * @param <T>  The persistent type
 * @param <ID> The primary key type
 * @author Trifork
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    // ~ Instance fields
    // --------------------------------------------------------

    private final Class<T> persistentClass;
    private EntityManager entityManager;

    // ~ Constructors
    // -----------------------------------------------------------

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public GenericDAOImpl(final Class<T> persistentClass) {
        super();
        this.persistentClass = persistentClass;
    }

    // ~ Methods
    // ----------------------------------------------------------------

    /**
     * @see GenericDao#countAll()
     */
    @Override
    public long countAll() {
        return countByCriteria();
    }

    /**
     * @see GenericDao#countByExample(java.lang.Object)
     */
    @Override
    public int countByExample(Example example) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = session.createCriteria(getEntityClass());
        crit.setProjection(Projections.rowCount());
        crit.add(example);
        return (Integer) crit.list().get(0);
    }

    /**
     * @see GenericDao#countByExample(java.lang.Object)
     */
    @Override
    public int countByExample(final T exampleInstance) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = session.createCriteria(getEntityClass());
        crit.setProjection(Projections.rowCount());
        crit.add(Example.create(exampleInstance));

        return (Integer) crit.list().get(0);
    }

    /**
     * DetachedCriteria query = DetachedCriteria.forClass(Cat.class).add( Property.forName("sex").eq('F') );
     *
     * @see GenericDao#countByDetachedCriteria(DetachedCriteria)
     */
    @Override
    public int countByDetachedCriteria(DetachedCriteria detachedCriteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = detachedCriteria.getExecutableCriteria(session);
        crit.setProjection(Projections.rowCount());
        return (Integer) crit.list().get(0);
    }

    /**
     * @see GenericDao#findAll()
     */
    @Override
    public List<T> findAll() {
        return findByCriteria();
    }

    /**
     * @see GenericDao#findByExample(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByExample(final T exampleInstance) {
        Session session = (Session) getEntityManager().getDelegate();
        final Example example = Example.create(exampleInstance).excludeZeroes();
        Criteria crit = session.createCriteria(getEntityClass()).add(example);
        return crit.list();
    }

    /**
     * @see GenericDao#findById(java.io.Serializable)
     */
    @Override
    public T findById(final ID id) {
        return getEntityManager().find(persistentClass, id);
    }

    @Override
    public T findById(final ID id, boolean writeLock) {
        return findById(id, LockModeType.PESSIMISTIC_WRITE);
    }

    private T findById(final ID id, LockModeType lockModeType) {
        return getEntityManager().find(persistentClass, id, lockModeType);
    }

    /**
     * @see GenericDao#findByNamedQuery(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByNamedQuery(final String name, Object... params) {
        return findByNamedQuery(name, (Integer) null, params);
    }

    /**
     * @see GenericDao#findByNamedQuery(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByNamedQuery(final String name, Integer maxResults, Object... params) {
        Query query = getEntityManager().createNamedQuery(name);

        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }

        return (List<T>) query.getResultList();
    }

    /**
     * @see GenericDao#findByNamedQuery(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <X> List<X> findByNamedQuery(final String name, Class<X> returnValueClass, Object... params) {
        javax.persistence.Query query = getEntityManager().createNamedQuery(name);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }

        return (List<X>) query.getResultList();
    }

    @Override
    public T findByNamedQueryUnique(final String name, Object... params) {
        return findByNamedQueryUnique(name, null, params);
    }

    @Override
    public T findByNamedQueryUnique(final String name, Integer maxResults, Object... params) {
        final List<T> results = findByNamedQuery(name, maxResults, params);
        if (results.size() > 1) {
            throw new RuntimeException("Expected at most 1 entity to match params='" + params + "'. Found " + results.size() + " matches.");
        }
        T result = null;
        if (results.size() == 1) {
            result = results.get(0);
        }
        return result;
    }

    /**
     * @see GenericDao#findByNamedQueryAndNamedParams(java.lang.String, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByNamedQueryAndNamedParams(final String name, final Map<String, ? extends Object> params) {
        javax.persistence.Query query = getEntityManager().createNamedQuery(name);

        for (final Map.Entry<String, ? extends Object> param : params
                .entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return (List<T>) query.getResultList();
    }

    /**
     * @see GenericDao#getEntityClass()
     */
    @Override
    public Class<T> getEntityClass() {
        return persistentClass;
    }

    /**
     * set the JPA entity manager to use.
     *
     * @param entityManager
     */
    @Required
    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    protected List<T> findByCriteria(final Criterion... criterion) {
        return findByCriteria(-1, -1, criterion);
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(final int firstResult, final int maxResults, final Criterion... criterion) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = session.createCriteria(getEntityClass());

        for (final Criterion c : criterion) {
            crit.add(c);
        }

        if (firstResult > 0) {
            crit.setFirstResult(firstResult);
        }

        if (maxResults > 0) {
            crit.setMaxResults(maxResults);
        }

        return crit.list();
    }

    /**
     * Use this inside subclasses as a convenience method.
     * E.g. DetachedCriteria query = DetachedCriteria.forClass(Cat.class).add( Property.forName("sex").eq('F') );
     *
     * @param detachedCriteria the search criteria
     * @return the list of entities
     */
    protected List<T> findByDetachedCriteria(DetachedCriteria detachedCriteria) {
        return findByDetachedCriteria(-1, -1, detachedCriteria);
    }

    /**
     * Use this inside subclasses as a convenience method.
     * E.g. DetachedCriteria query = DetachedCriteria.forClass(Cat.class).add( Property.forName("sex").eq('F') );
     *
     * @param firstResult
     * @param maxResults
     * @param detachedCriteria the search criteria
     * @return the list of entities
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByDetachedCriteria(int firstResult, int maxResults, DetachedCriteria detachedCriteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = detachedCriteria.getExecutableCriteria(session);
        if (firstResult > 0) {
            crit.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            crit.setMaxResults(maxResults);
        }
        return crit.list();
    }


    protected long countByCriteria(Criterion... criterion) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = session.createCriteria(getEntityClass());
        crit.setProjection(Projections.rowCount());

        for (final Criterion c : criterion) {
            crit.add(c);
        }

        return (Long) crit.list().get(0);
    }


    /**
     * @see GenericDao#delete(java.lang.Object)
     */
    @Override
    public void delete(T entity) {
        getEntityManager().remove(entity);
    }

    /**
     * @see GenericDao#save(java.lang.Object)
     */
    @Override
    public T save(T entity) {
        return getEntityManager().merge(entity);
    }

    @Override
    public void create(T entity) {
        getEntityManager().persist(entity);
    }
}
