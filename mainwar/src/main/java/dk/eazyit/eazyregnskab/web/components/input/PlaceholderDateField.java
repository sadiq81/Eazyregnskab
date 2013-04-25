package dk.eazyit.eazyregnskab.web.components.input;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author Trifork
 */
public class PlaceholderDateField extends DateTextField {

    static final Logger LOG = LoggerFactory.getLogger(PlaceholderDateField.class);

    public PlaceholderDateField(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, String datePattern) {
        super(markupId, datePattern);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, IModel<Date> model) {
        super(markupId, model);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, IModel<Date> model, String dateFormat) {
        super(markupId, model, dateFormat);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, IModel<Date> model, DateTextFieldConfig config) {
        super(markupId, model, config);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, DateTextFieldConfig config) {
        super(markupId, config);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }
}
