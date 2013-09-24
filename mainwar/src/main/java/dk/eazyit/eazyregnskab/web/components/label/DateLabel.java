package dk.eazyit.eazyregnskab.web.components.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class DateLabel extends Label {

    private static final Logger LOG = LoggerFactory.getLogger(DateLabel.class);

    public DateLabel(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public DateLabel(String id, String label) {
        super(id, label);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public DateLabel(String id, Serializable label) {
        super(id, label);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());

    }

    public DateLabel(String id, IModel<?> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>) new DateConverter();
    }
}
