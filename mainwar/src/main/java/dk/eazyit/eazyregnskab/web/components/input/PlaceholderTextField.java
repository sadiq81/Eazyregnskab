package dk.eazyit.eazyregnskab.web.components.input;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Trifork
 */
public class PlaceholderTextField<T> extends TextField<T> {

    static final Logger LOG = LoggerFactory.getLogger(PlaceholderTextField.class);

    String localizerPage = "";

    public PlaceholderTextField(String id) {
        this(id, "");
    }

    public PlaceholderTextField(String id, String localizerPage) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        this.localizerPage = localizerPage.length() > 0 ? localizerPage + "." : localizerPage;
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderTextField(String id, Class type) {
        super(id, type);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderTextField(String id, IModel model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public PlaceholderTextField(String id, IModel model, Class type) {
        super(id, model, type);
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(localizerPage + this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }
}
