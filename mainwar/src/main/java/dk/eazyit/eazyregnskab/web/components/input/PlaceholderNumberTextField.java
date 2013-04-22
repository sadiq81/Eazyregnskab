package dk.eazyit.eazyregnskab.web.components.input;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Trifork
 */
public class PlaceholderNumberTextField<T extends Number & Comparable<T>> extends NumberTextField<T> {

    static final Logger LOG = LoggerFactory.getLogger(PlaceholderNumberTextField.class);

    public PlaceholderNumberTextField(String id) {
        super(id);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderNumberTextField(String id, IModel model) {
        super(id, model);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderNumberTextField(String id, IModel model, Class type) {
        super(id, model, type);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
        tag.put("step", "any");
    }


    @Override
    public <T> IConverter<T> getConverter(Class<T> type) {
        return super.getConverter(type);
    }
}
