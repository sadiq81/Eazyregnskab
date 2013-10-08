package dk.eazyit.eazyregnskab.web.components.lists.listeditor;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;

import java.util.List;

/**
 * @author
 */
public abstract class EditorButton extends AjaxButton {

    private transient ListItem<?> parent;

    public EditorButton(String id) {
        super(id);
    }

    protected final ListItem<?> getItem() {
        if (parent == null) {
            parent = findParent(ListItem.class);
        }
        return parent;
    }

    protected final List<?> getList() {
        return getEditor().items;
    }

    protected final ListEditor<?> getEditor() {
        return (ListEditor<?>) getItem().getParent();
    }

    protected void onDetach() {
        parent = null;
        super.onDetach();
    }
}
