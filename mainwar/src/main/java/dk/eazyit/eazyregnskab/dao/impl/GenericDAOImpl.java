package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.GenericDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @author Eazy IT
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    // ~ Instance fields
    // --------------------------------------------------------

    final Log logger = LogFactory.getLog(this.getClass());
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
        logger.debug("Counting " + persistentClass.getName() + " all");
        return countByCriteria();
    }

    /**
     * @see GenericDao#countByExample(java.lang.Object)
     */
    @Override
    public int countByExample(Example example) {
        logger.debug("Counting " + persistentClass.getName() + " from database by example" + example.toString());
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
        logger.debug("Counting " + persistentClass.getName() + " from database by example" + exampleInstance.toString());
        Session session = (Session) getEntityManager().getDelegate();
        Criteria crit = session.createCriteria(getEntityClass());
        crit.setProjection(Projections.rowCount());
        crit.add(Example.create(exampleInstance));

        if (crit.list().get(0) instanceof Long) {
            return ((Long) crit.list().get(0)).intValue();
        } else if (crit.list().get(0) instanceof Integer) {
            return (Integer) crit.list().get(0);
        } else {
            logger.warn(getClass().toString() + " countByExample returned something different than int or long");
            return 0;
        }
    }

    /**
     * DetachedCriteria query = DetachedCriteria.forClass(Cat.class).add( Property.forName("sex").eq('F') );
     *
     * @see GenericDao#countByDetachedCriteria(DetachedCriteria)
     */
    @Override
    public int countByDetachedCriteria(DetachedCriteria detachedCriteria) {
        logger.debug("Counting all of " + persistentClass.getName() + " by  detached criteria " + detachedCriteria.toString());
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
        logger.debug("Finding all of " + persistentClass.getName() + " from database");
        return findByCriteria();
    }

    /**
     * @see GenericDao#findByExample(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByExample(final T exampleInstance) {
        logger.debug("Finding all " + persistentClass.getName() + " from database by example" + exampleInstance.toString());
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
        logger.debug("Finding " + persistentClass.getName() + " from database by id" + id);
        return getEntityManager().find(persistentClass, id);
    }

    @Override
    public T findById(final ID id, boolean writeLock) {
        logger.debug("Finding " + persistentClass.getName() + " from database by id" + id);
        return findById(id, LockModeType.PESSIMISTIC_WRITE);
    }

    private T findById(final ID id, LockModeType lockModeType) {
        logger.debug("Finding " + persistentClass.getName() + " from database by id" + id);
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
        logger.debug("Finding list" + persistentClass.getName() + " from database by named query" + name);
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
    public List<T> findByNamedQuery(final String name, Integer firstResult, Integer maxResults, Object... params) {
        logger.debug("Finding list" + persistentClass.getName() + " from database by named query" + name);
        Query query = getEntityManager().createNamedQuery(name);

        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }

        if (firstResult != null) {
            query.setFirstResult(firstResult);
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
        logger.debug("Finding list" + persistentClass.getName() + " from database by named query " + name);
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
        logger.debug("Finding list" + persistentClass.getName() + " from database");
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
    @Override
    public List<T> findByCriteria(final int firstResult, final int maxResults, final Criterion... criterion) {
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
        logger.debug("Deleting " + entity.toString() + " from database");
        entity = getEntityManager().merge(entity);
        getEntityManager().remove(entity);
    }

    /**
     * @see GenericDao#save(java.lang.Object)
     */
    @Override
    public T save(T entity) {
        logger.debug("Saving " + entity.toString() + " to database");
        return getEntityManager().merge(entity);
    }


    @Override
    public void create(T entity) {
        logger.debug("Creating " + entity.toString() + " to database");
        getEntityManager().persist(entity);
    }
}
