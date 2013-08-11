package dk.eazyit.eazyregnskab.web.components.label;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;

/**
 * @author
 */
public class ToolTipTop {

    public static Component addToolTipToComponent(Component component, String tooltip) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", tooltip));
        return component;
    }
}
