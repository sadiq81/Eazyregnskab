package dk.eazyit.eazyregnskab.dao.interfaces;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Generic Repository, providing basic CRUD operations
 *
 * @param <T>  the entity type
 * @param <ID> the primary key type
 * @author Jurgen Lust
 * @see <a href="http://www.bejug.org/confluenceBeJUG/display/BeJUG/Generic+DAO+example">Source</a>
 */
public interface GenericDAO<T, ID extends Serializable> {
    //~ Methods ----------------------------------------------------------------

    /**
     * Get the Class of the entity
     *
     * @return the class
     */
    Class<T> getEntityClass();

    /**
     * Find an entity by its primary key
     *
     * @param id the primary key
     * @return the entity
     */
    T findById(final ID id);

    /**
     * Find an entity by its primary key and if specified obtain a write lock
     *
     * @param id        the primary key
     * @param writeLock obtain a write lock for the found entity
     * @return the entity
     */
    T findById(final ID id, boolean writeLock);

    /**
     * Load all entities
     *
     * @return the list of entities
     */
    List<T> findAll();

    /**
     * Find entities based on an example
     *
     * @param exampleInstance the example
     * @return the list of entities
     */
    List<T> findByExample(final T exampleInstance);

    /**
     * Find using a named query
     *
     * @param queryName the name of the query
     * @param params    the query parameters
     * @return the list of entities
     */
    List<T> findByNamedQuery(final String queryName, Object... params);

    /**
     * Find using a named query
     *
     * @param queryName  the name of the query
     * @param maxResults Max rows to return
     * @param params     the query parameters
     * @return the list of entities
     */
    List<T> findByNamedQuery(String queryName, Integer maxResults, Object... params);

    /**
     * Find using a named query
     *
     * @param queryName   the name of the query
     * @param firstResult First result to return
     * @param maxResults  Max rows to return
     * @param params      the query parameters
     * @return the list of entities
     */
    List<T> findByNamedQuery(String queryName, Integer firstResult, Integer maxResults, Object... params);

    <X> List<X> findByNamedQuery(final String name, Class<X> returnValueClass, Object... params);

    /**
     * Find using a named query
     *
     * @param queryName the name of the query
     * @param params    the query parameters
     * @return the list of entities
     */
    List<T> findByNamedQueryAndNamedParams(
            final String queryName,
            final Map<String, ? extends Object> params
    );

    /**
     * Find using a named query and expect at most 1 result
     *
     * @param name   the name of the query
     * @param params the query parameters
     * @return the matching entity or null if no match found
     */
    T findByNamedQueryUnique(final String name, Object... params);

    /**
     * Count all entities
     *
     * @return the number of entities
     */
    long countAll();

    /**
     * Count entities based on an example
     *
     * @param example the search criteria
     * @return the number of entities
     */
    int countByExample(Example example);

    /**
     * Count entities based on an example
     *
     * @param exampleInstance the search criteria
     * @return the number of entities
     */
    int countByExample(final T exampleInstance);

    /**
     * E.g. DetachedCriteria query = DetachedCriteria.forClass(Cat.class).add( Property.forName("sex").eq('F') );
     *
     * @param detachedCriteria the search criteria
     * @return the number of entities
     */
    int countByDetachedCriteria(final DetachedCriteria detachedCriteria);

    public EntityManager getEntityManager();

    List<T> findByCriteria(final int firstResult, final int maxResults, final Criterion... criterion);

    /**
     * save an entity. This can be either a INSERT or UPDATE in the database
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    T save(final T entity);

    /**
     * @param entity the entity to create
     */
    void create(final T entity);

    /**
     * delete an entity from the database
     *
     * @param entity the entity to delete
     */
    void delete(final T entity);

    T findByNamedQueryUnique(String name, Integer maxResults, Object... params);
}
