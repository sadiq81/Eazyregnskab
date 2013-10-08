package dk.eazyit.eazyregnskab.web.components.lists.listeditor;

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public abstract class ListEditor<T> extends RepeatingView implements IFormModelUpdateListener {

    List<T> items;

    public ListEditor(String id, IModel<List<T>> model) {
        super(id, model);
    }

    protected abstract void onPopulateItem(ListItem<T> item);

    public void addItem(T value) {
        items.add(value);
        ListItem<T> item = new ListItem<T>(newChildId(), items.size() - 1);
        add(item);
        onPopulateItem(item);
    }

    protected void onBeforeRender() {
        if (!hasBeenRendered()) {
            items = new ArrayList<>((List<T>) getDefaultModelObject());
            for (int i = 0; i < items.size(); i++) {
                ListItem<T> li = new ListItem<T>(newChildId(), i);
                li.setModel(new CompoundPropertyModel<T>(items.get(i)));
                add(li);
                onPopulateItem(li);
            }
        }
        super.onBeforeRender();
    }

    public void updateModel() {
        setDefaultModelObject(items);
    }

    public List<T> getItems() {
        return items;
    }
}
