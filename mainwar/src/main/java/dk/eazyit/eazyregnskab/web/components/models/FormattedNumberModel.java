package dk.eazyit.eazyregnskab.web.components.models;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * @author http://stackoverflow.com/questions/3395825/how-to-print-formatted-bigdecimal-values
 *         http://stackoverflow.com/questions/8658205/format-currency-without-currency-symbol
 */
public class FormattedNumberModel extends Model<String> {


    private static final long serialVersionUID = 1L;
    private IModel<Date> model;
    Integer maximumFractionDigits;
    Integer minimumFractionDigits;

    public FormattedNumberModel(IModel<Date> model, Integer minimumFractionDigits, Integer maximumFractionDigits) {
        this.model = model;
        this.minimumFractionDigits = minimumFractionDigits;
        this.maximumFractionDigits = minimumFractionDigits;
    }

    @Override
    public String getObject() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Session.get().getLocale());
        formatter.setMaximumFractionDigits(maximumFractionDigits);
        formatter.setMinimumFractionDigits(minimumFractionDigits);
        return formatter.format(model.getObject());
    }

}


