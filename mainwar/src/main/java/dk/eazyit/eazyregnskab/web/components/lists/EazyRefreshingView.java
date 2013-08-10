package dk.eazyit.eazyregnskab.web.components.lists;

import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author
 */
public abstract class EazyRefreshingView<T> extends RefreshingView<T> {

    public EazyRefreshingView(String id) {
        super(id);
    }

    public EazyRefreshingView(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected Iterator<IModel<T>> getItemModels() {

        List<IModel<T>> list = new ArrayList<IModel<T>>();
        for (T entity : getListOfItems()) {
            list.add(new CompoundPropertyModel<T>(entity));
        }
        return list.iterator();
    }

    protected abstract List<T> getListOfItems();
}
