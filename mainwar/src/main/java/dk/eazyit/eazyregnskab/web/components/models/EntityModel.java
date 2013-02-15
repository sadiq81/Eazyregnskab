package dk.eazyit.eazyregnskab.web.components.models;

import java.io.Serializable;

/**
 * @author Trifork
 */
public class EntityModel<T extends Identifiable<?>> extends AbstractEntityModel<T> {


    public EntityModel(T entity) {
        super(entity);
    }

    public EntityModel(Class<? extends T> clazz, Serializable id) {
        super(clazz, id);
    }

    //    @Dependency
//    private Session session;

    @Override
    protected T load(Class clazz, Serializable id) {
//        return session.get(clazz, id);
        return null;
    }
}
