package dk.eazyit.eazyregnskab.web.components.page;

import dk.eazyit.eazyregnskab.util.CalendarUtil;
import dk.eazyit.eazyregnskab.util.ReportObject;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author
 */
public abstract class BaseReportPage extends LoggedInPage {


    protected ReportObject reportObject;

    protected BaseReportPage() {
    }

    protected BaseReportPage(IModel<?> model) {
        super(model);
    }

    protected BaseReportPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);
        setDefaultModel(new CompoundPropertyModel<ReportObject>(reportObject = new ReportObject(
                CalendarUtil.getFirstDayInYear(),
                CalendarUtil.getLastDayInYear(),
                financeAccountService.findLowestFinanceAccountByLegalEntity(getCurrentLegalEntity()),
                financeAccountService.findHighestFinanceAccountByLegalEntity(getCurrentLegalEntity()))));
    }

}
