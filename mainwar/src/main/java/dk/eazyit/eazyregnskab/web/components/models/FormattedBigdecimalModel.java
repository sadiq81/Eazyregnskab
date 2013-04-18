package dk.eazyit.eazyregnskab.web.components.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.text.NumberFormat;
import java.util.Date;

/**
 * @author
 */
public class FormattedBigdecimalModel extends Model<String> {

    private static final long serialVersionUID = 1L;
    private IModel<Date> model;


    public FormattedBigdecimalModel(IModel<Date> model) {
        this.model = model;
    }

    @Override
    public String getObject() {
        //http://stackoverflow.com/questions/3395825/how-to-print-formatted-bigdecimal-values
        return NumberFormat.getCurrencyInstance().format(model.getObject());
    }

}


