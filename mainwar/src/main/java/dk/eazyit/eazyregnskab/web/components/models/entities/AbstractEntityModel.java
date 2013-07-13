package dk.eazyit.eazyregnskab.web.components.models.entities;

import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import org.apache.wicket.model.IModel;

import javax.persistence.EntityNotFoundException;

/**
 * @author Eazy It
 */
public abstract class AbstractEntityModel<T extends EntityWithLongId> implements IModel<T> {

    private final Class clazz;
    protected Long id;
    protected T entity;

    public AbstractEntityModel(T entity) {
        clazz = entity.getClass();
        id = entity.getId();
        this.entity = entity;
    }

    public AbstractEntityModel(Class<? extends T> clazz, Long id) {
        this.clazz = clazz;
        this.id = id;
    }

    public T getObject() {
        if (entity == null) {
            if (id != null) {
                entity = load(id);
                if (entity == null) {
                    throw new EntityNotFoundException("Class :" + clazz.toString() + " id: " + id.toString());
                }
            }
        }
        return entity;
    }

    public void detach() {
        if (entity != null) {
            if (entity.getId() != 0 && entity.getId() != null) {
                id = entity.getId();
                entity = null;
            }
        }
    }

    protected abstract T load(Long id);

    public abstract void setObject(T object);

}