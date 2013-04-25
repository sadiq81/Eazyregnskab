package dk.eazyit.eazyregnskab.web.components.input;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Trifork
 */
public class PlaceholderPasswordField extends PasswordTextField {

    String placeholder;
    static final Logger LOG = LoggerFactory.getLogger(PlaceholderPasswordField.class);

    public PlaceholderPasswordField(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderPasswordField(String id, IModel<String> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put("placeholder", placeholder);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
