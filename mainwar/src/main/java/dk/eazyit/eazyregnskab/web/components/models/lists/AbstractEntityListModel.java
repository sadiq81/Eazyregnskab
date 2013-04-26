package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eazy It
 */
public abstract class AbstractEntityListModel<T extends EntityWithLongId, E extends EntityWithLongId> implements IModel<List<T>> {

    private E parent = null;
    private List<T> list = null;

    protected AbstractEntityListModel() {
    }

    public AbstractEntityListModel(E parent) {
        this.parent = parent;
    }

    public AbstractEntityListModel(E parent, List<T> list) {
        this.parent = parent;
        this.list = list;
    }

    public List<T> getObject() {
        if (list == null) {
            if (parent == null) {
                list = load(parent = fetchParent());
            } else {
                list = load(parent);
            }
            if (list == null) {
                return new ArrayList<T>();
            }
        }
        return list;
    }


    public void detach() {
        if (list != null) {
            if (parent != null) {
                parent = null;
            }
            list = null;
        }
    }

    protected abstract List<T> load(E id);

    protected abstract E fetchParent();

    public abstract void setObject(List<T> object);

}