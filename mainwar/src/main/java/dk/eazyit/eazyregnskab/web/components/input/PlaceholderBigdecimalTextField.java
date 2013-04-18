package dk.eazyit.eazyregnskab.web.components.input;

import dk.eazyit.eazyregnskab.util.BigDecimalRangeValidator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import java.math.BigDecimal;

/**
 * @author Trifork
 */
public class PlaceholderBigdecimalTextField extends NumberTextField<BigDecimal> {

    public PlaceholderBigdecimalTextField(String id) {
        super(id);
    }

    public PlaceholderBigdecimalTextField(String id, IModel model) {
        super(id, model);
    }

    public PlaceholderBigdecimalTextField(String id, IModel model, Class type) {
        super(id, model, type);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        String placeholder = getLocalizer().getString(this.getId(), this.getPage());
        tag.put("placeholder", placeholder);
    }


    @Override
    public <T> IConverter<T> getConverter(Class<T> type) {
        return super.getConverter(type);
    }

    @Override
    public void onConfigure() {
        add(new BigDecimalRangeValidator());
    }
}
