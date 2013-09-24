package dk.eazyit.eazyregnskab.web.components.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class ToolTipLabel extends Label {

    private static final Logger LOG = LoggerFactory.getLogger(ToolTipLabel.class);

    public ToolTipLabel(String id, String tooltip) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }

    public ToolTipLabel(String id, String label, String tooltip) {
        super(id, label);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }

    public ToolTipLabel(String id, Serializable label, String tooltip) {
        super(id, label);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }

    public ToolTipLabel(String id, IModel<?> model, String tooltip) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
        ToolTipTop.addToolTipToComponent(this, tooltip);
    }
}
