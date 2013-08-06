package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.services.FiscalYearService;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import dk.eazyit.eazyregnskab.util.ReportObject;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author
 */
public class BalanceReportFormValidator extends BaseFormValidator {

    @SpringBean
    FiscalYearService fiscalYearService;

    @Override
    public void validate(Form<?> form) {

        ReportObject reportObject = (ReportObject) form.getModelObject();

        List<FiscalYear> fiscalYearList = fiscalYearService.findFiscalYearByLegalEntity(getCurrentLegalEntity());

        boolean withIn = false;
        for (FiscalYear fiscalYear : fiscalYearList) {

            if (CalendarUtil.betweenDates(reportObject.getDateFrom(), fiscalYear) && CalendarUtil.betweenDates(reportObject.getDateTo(), fiscalYear)) {
                withIn = true;
                break;
            }
        }
        if (!withIn) {
            error((FormComponent<?>) form.get("dateFrom"), "dates.not.within.same.fiscal.year");
        }

    }
}
