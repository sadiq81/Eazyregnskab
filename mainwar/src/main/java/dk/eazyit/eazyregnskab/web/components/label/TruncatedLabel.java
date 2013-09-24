package dk.eazyit.eazyregnskab.web.components.label;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class TruncatedLabel extends Label {

    int truncate;
    private static final Logger LOG = LoggerFactory.getLogger(TruncatedLabel.class);

    public TruncatedLabel(String id, int truncate) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        this.truncate = truncate;
    }

    public TruncatedLabel(String id, String label, int truncate) {
        super(id, label);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        this.truncate = truncate;
    }

    public TruncatedLabel(String id, Serializable label, int truncate) {
        super(id, label);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        this.truncate = truncate;
    }

    public TruncatedLabel(String id, IModel<?> model, int truncate) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
        this.truncate = truncate;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        String str = getDefaultModelObjectAsString();
        if (str.length() > truncate) {
            addToolTipToComponent(this, str);
        }
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {

        String str = getDefaultModelObjectAsString();
        if (str.length() > truncate) {
            str = str.substring(0, truncate) + "...";
        }
        replaceComponentTagBody(markupStream, openTag, str);
    }

    protected void addToolTipToComponent(Component component, String text) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", text));
    }
}
