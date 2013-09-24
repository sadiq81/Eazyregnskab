package dk.eazyit.eazyregnskab.web.components.link;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class AjaxToolTipLink<T> extends AjaxLink<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AjaxToolTipLink.class);

    protected AjaxToolTipLink(String id, String resourceText) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        add(AttributeModifier.append("rel", "tooltip"));
        add(AttributeModifier.append("data-placement", "top"));
        add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }
}
