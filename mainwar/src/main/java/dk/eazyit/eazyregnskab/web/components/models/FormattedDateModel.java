package dk.eazyit.eazyregnskab.web.components.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 */
public class FormattedDateModel extends Model<String> {

    private static final long serialVersionUID = 1L;
    private IModel<Date> model;

    private static final String DATE_FORMAT = "dd-MM-yy";
    private static SimpleDateFormat sdf = null;

    public FormattedDateModel(IModel<Date> model) {
        this.model = model;
        sdf = new SimpleDateFormat(DATE_FORMAT);
    }

    public FormattedDateModel(IModel<Date> model, String pattern) {
        this.model = model;
        sdf = new SimpleDateFormat(pattern);
    }

    @Override
    public String getObject() {
        return sdf.format(model.getObject());
    }

}


