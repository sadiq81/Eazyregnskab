package dk.eazyit.eazyregnskab.web.components.input;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author Eazy IT
 */
public class PlaceholderDateField extends DateTextField {

    static final Logger LOG = LoggerFactory.getLogger(PlaceholderDateField.class);

    public PlaceholderDateField(String markupId, DateTextFieldConfig config) {
        super(markupId, config);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, IModel<Date> model, DateTextFieldConfig config) {
        super(markupId, model, config);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }
}
