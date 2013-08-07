package dk.eazyit.eazyregnskab.web.components.label;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;

import java.io.Serializable;

/**
 * @author
 */
public class DateLabel extends Label{

    public DateLabel(String id) {
        super(id);
    }

    public DateLabel(String id, String label) {
        super(id, label);
    }

    public DateLabel(String id, Serializable label) {
        super(id, label);
    }

    public DateLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public <C> IConverter<C> getConverter(Class<C> type) {
        return (IConverter<C>) new DateConverter();
    }
}
