package dk.eazyit.eazyregnskab.web.components.link;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class ToolTipLink extends Link {

    private static final Logger LOG = LoggerFactory.getLogger(ToolTipLink.class);

    protected ToolTipLink(String id, String resourceText) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        add(AttributeModifier.append("rel", "tooltip"));
        add(AttributeModifier.append("data-placement", "top"));
        add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }
}
