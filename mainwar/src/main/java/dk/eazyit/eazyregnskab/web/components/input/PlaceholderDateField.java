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

    String localizerPage = "";

    public PlaceholderDateField(String id, DateTextFieldConfig config) {
        this(id, "", config);
    }

    public PlaceholderDateField(String markupId, String localizerPage, DateTextFieldConfig config) {
        super(markupId, config);
        setOutputMarkupPlaceholderTag(true);
        this.localizerPage = localizerPage.length() > 0 ? localizerPage + "." : localizerPage;
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderDateField(String markupId, String localizerPage, IModel<Date> model, DateTextFieldConfig config) {
        super(markupId, model, config);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(localizerPage + this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }
}
