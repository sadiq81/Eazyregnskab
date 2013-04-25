package dk.eazyit.eazyregnskab.web.components.input;

import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class DisabledPlaceHolderTextField<T> extends PlaceholderTextField<T> {

    public DisabledPlaceHolderTextField(String id) {
        super(id);
    }

    public DisabledPlaceHolderTextField(String id, Class type) {
        super(id, type);
    }

    public DisabledPlaceHolderTextField(String id, IModel model) {
        super(id, model);
    }

    public DisabledPlaceHolderTextField(String id, IModel model, Class type) {
        super(id, model, type);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
