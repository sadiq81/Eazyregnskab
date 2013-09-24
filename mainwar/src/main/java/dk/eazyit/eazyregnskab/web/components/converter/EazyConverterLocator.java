package dk.eazyit.eazyregnskab.web.components.converter;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.util.convert.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class EazyConverterLocator extends ConverterLocator {

    private static final Logger LOG = LoggerFactory.getLogger(EazyConverterLocator.class);

    public EazyConverterLocator() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName());
        set(Double.TYPE, new EazyDoubleConverter());
        set(Double.class, new EazyDoubleConverter());
        set(java.sql.Date.class, new DateConverter());
        set(java.sql.Time.class, new DateConverter());
        set(java.sql.Timestamp.class, new DateConverter());
    }
}
