package dk.eazyit.eazyregnskab.web.components.converter;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.util.convert.converter.DateConverter;

/**
 * @author
 */
public class EazyConverterLocator extends ConverterLocator {

    public EazyConverterLocator() {
        super();
        set(Double.TYPE, new EazyDoubleConverter());
        set(Double.class, new EazyDoubleConverter());
        set(java.sql.Date.class, new DateConverter());
        set(java.sql.Time.class, new DateConverter());
        set(java.sql.Timestamp.class, new DateConverter());
    }
}
