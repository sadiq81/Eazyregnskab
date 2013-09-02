package dk.eazyit.eazyregnskab.web.components.converter;

import org.apache.wicket.util.convert.converter.DoubleConverter;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author
 */
public class EazyDoubleConverter extends DoubleConverter {

    @Override
    public String convertToString(Double aDouble, Locale locale) {
        NumberFormat nf = this.getNumberFormat(locale);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return nf.format(aDouble);
    }

}