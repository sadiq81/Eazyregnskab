package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class FiscalYearFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public FiscalYearFormValidator(FormComponent... components) {
        super(components);
    }

    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> formComponent1 = getDependentFormComponents()[0];
        final FormComponent<?> formComponent2 = getDependentFormComponents()[1];

        Date start = (Date) formComponent1.getConvertedInput();
        Date end = (Date) formComponent2.getConvertedInput();
        FiscalYear current = new FiscalYear(start, end);

        if (end.before(start)) {
            error(formComponent1, "start.date.must.be.before.end.date");
        }
        List<FiscalYear> list = new ArrayList<FiscalYear>(fiscalYearService.findFiscalYearByLegalEntity(getCurrentLegalEntity()));
        list.remove(form.getModelObject());

        for (FiscalYear fiscalYear : list) {

            //Test dates in new year to see if they are between another finance year
            if (betweenDates(start, fiscalYear) || betweenDates(end, fiscalYear)) {
                error(formComponent1, "dates.cross.another.fiscal.year");
                break;
            }
            //Test dates in new year to see if old years are between new year
            if (betweenDates(fiscalYear.getStart(), current) || betweenDates(fiscalYear.getEnd(), current)) {
                error(formComponent1, "dates.cross.another.fiscal.year");
                break;
            }
        }
    }

    private boolean betweenDates(Date testDate, FiscalYear fiscalYear) {
        return testDate.compareTo(fiscalYear.getStart()) >= 0 && testDate.compareTo(fiscalYear.getEnd()) <= 0;
    }
}
