package dk.eazyit.eazyregnskab.web.components.lists.listeditor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * @author
 */
public class RemoveButton extends EditorButton {

    public RemoveButton(String id) {
        super(id);
        setDefaultFormProcessing(false);
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        super.onSubmit(target, form);

        int idx = ((ListItem) getParent()).getIndex();
        for (int i = idx + 1; i < getItem().getParent().size(); i++) {
            ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
            item.setIndex(item.getIndex() - 1);
        }

        getList().remove(idx);
        getEditor().remove(getItem());
    }

    @Override
    protected void onError(AjaxRequestTarget target, Form<?> form) {
        super.onError(target, form);
    }
}
