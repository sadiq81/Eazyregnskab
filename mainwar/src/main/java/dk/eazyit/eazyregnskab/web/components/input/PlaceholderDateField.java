package dk.eazyit.eazyregnskab.web.components.input;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;

import java.util.Date;

/**
 * @author Trifork
 */
public class PlaceholderDateField extends DateTextField {

    public PlaceholderDateField(String id) {
        super(id);
    }

    public PlaceholderDateField(String markupId, String datePattern) {
        super(markupId, datePattern);
    }

    public PlaceholderDateField(String markupId, IModel<Date> model) {
        super(markupId, model);
    }

    public PlaceholderDateField(String markupId, IModel<Date> model, String dateFormat) {
        super(markupId, model, dateFormat);
    }

    public PlaceholderDateField(String markupId, IModel<Date> model, DateTextFieldConfig config) {
        super(markupId, model, config);
    }

    public PlaceholderDateField(String markupId, DateTextFieldConfig config) {
        super(markupId, config);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }
}
