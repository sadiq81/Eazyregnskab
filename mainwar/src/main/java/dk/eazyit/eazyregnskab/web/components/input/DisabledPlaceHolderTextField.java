package dk.eazyit.eazyregnskab.web.components.input;

import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DisabledPlaceHolderTextField<T> extends PlaceholderTextField<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DisabledPlaceHolderTextField.class);

    public DisabledPlaceHolderTextField(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public DisabledPlaceHolderTextField(String id, Class type) {
        super(id, type);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public DisabledPlaceHolderTextField(String id, IModel model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    public DisabledPlaceHolderTextField(String id, IModel model, Class type) {
        super(id, model, type);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
