package dk.eazyit.eazyregnskab.web.components.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

/**
 * @author
 */
public class ToolTipLabel extends Label {

    public ToolTipLabel(String id, String tooltip) {
        super(id);
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }

    public ToolTipLabel(String id, String label, String tooltip) {
        super(id, label);
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }

    public ToolTipLabel(String id, Serializable label, String tooltip) {
        super(id, label);
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }

    public ToolTipLabel(String id, IModel<?> model, String tooltip) {
        super(id, model);
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }
}
