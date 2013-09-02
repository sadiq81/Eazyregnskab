package dk.eazyit.eazyregnskab.web.components.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.RangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author
 */
public class BigDecimalRangeValidator extends RangeValidator<BigDecimal> {

    private static final Logger LOG = LoggerFactory.getLogger(BigDecimalRangeValidator.class);

    public BigDecimalRangeValidator() {
        LOG.trace("creating " + this.getClass().getSimpleName());
    }

    public BigDecimalRangeValidator(BigDecimal minimum, BigDecimal maximum) {
        super(minimum, maximum);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with minimum " + minimum + " and maximum " + maximum);
    }

    @Override
    public void validate(IValidatable<BigDecimal> validatable) {

        LOG.trace("validating" + validatable);

        BigDecimal value = getValue(validatable);
        final BigDecimal min = getMinimum();
        final BigDecimal max = getMaximum();
        if ((min != null && value.compareTo(min) < 0) || (max != null && value.compareTo(max) > 0)) {
            Mode mode = getMode();
            ValidationError error = new ValidationError(this, mode.getVariation());
            if (min != null) {
                error.setVariable("minimum", min);
            }
            if (max != null) {
                error.setVariable("maximum", max);
            }
            if (mode == Mode.EXACT) {
                error.setVariable("exact", max);
            }
            validatable.error(decorate(error, validatable));
        }
    }


}
