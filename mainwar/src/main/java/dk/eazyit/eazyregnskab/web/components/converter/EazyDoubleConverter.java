package dk.eazyit.eazyregnskab.web.components.converter;

import org.apache.wicket.util.convert.converter.DoubleConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author
 */
public class EazyDoubleConverter extends DoubleConverter {

    private static final Logger LOG = LoggerFactory.getLogger(EazyDoubleConverter.class);

    @Override
    public String convertToString(Double aDouble, Locale locale) {
        LOG.trace("converting " + aDouble + " to string, locale = " + locale);
        NumberFormat nf = this.getNumberFormat(locale);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return nf.format(aDouble);
    }

}