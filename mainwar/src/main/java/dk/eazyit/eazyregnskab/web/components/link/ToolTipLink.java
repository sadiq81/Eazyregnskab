package dk.eazyit.eazyregnskab.web.components.link;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public abstract class ToolTipLink extends Link {

    protected ToolTipLink(String id, String resourceText) {
        super(id);
        add(AttributeModifier.append("rel", "tooltip"));
        add(AttributeModifier.append("data-placement", "top"));
        add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }

    protected ToolTipLink(String id, IModel model) {
        super(id, model);
    }
}
