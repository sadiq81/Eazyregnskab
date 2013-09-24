package dk.eazyit.eazyregnskab.web.components.models.entities;

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

    private String DATE_FORMAT = "dd-MM-yy";

    public FormattedDateModel(IModel<Date> model) {
        this.model = model;
    }

    public FormattedDateModel(IModel<Date> model, String pattern) {
        this.model = model;
        DATE_FORMAT = pattern;
    }

    @Override
    public String getObject() {
        return new SimpleDateFormat(DATE_FORMAT).format(model.getObject());
    }

}


