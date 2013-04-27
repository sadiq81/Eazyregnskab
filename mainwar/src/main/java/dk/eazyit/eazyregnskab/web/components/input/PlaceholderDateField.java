package dk.eazyit.eazyregnskab.web.components.input;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextField;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Trifork
 */
public class PlaceholderDateField extends DateTextField {

    static final Logger LOG = LoggerFactory.getLogger(PlaceholderDateField.class);

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

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return super.getConverter(type);
    }

    @Override
    protected CharSequence createScript(DateTextFieldConfig config) {
        return super.createScript(config);
    }
}
