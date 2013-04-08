package dk.eazyit.eazyregnskab.web.components.input;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author Trifork
 */
public class PlaceholderTextField<T> extends TextField<T> {

    public PlaceholderTextField(String id) {
        super(id);
    }

    public PlaceholderTextField(String id, Class type) {
        super(id, type);
    }

    public PlaceholderTextField(String id, IModel model) {
        super(id, model);
    }

    public PlaceholderTextField(String id, IModel model, Class type) {
        super(id, model, type);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }
}
