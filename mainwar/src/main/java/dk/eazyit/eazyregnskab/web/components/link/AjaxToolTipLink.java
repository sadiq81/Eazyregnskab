package dk.eazyit.eazyregnskab.web.components.link;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public abstract class AjaxToolTipLink<T> extends AjaxLink<T> {

    protected AjaxToolTipLink(String id, String resourceText) {
        super(id);
        add(AttributeModifier.append("rel", "tooltip"));
        add(AttributeModifier.append("data-placement", "top"));
        add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }
}
