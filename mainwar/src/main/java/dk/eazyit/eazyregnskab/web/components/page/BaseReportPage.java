package dk.eazyit.eazyregnskab.web.components.page;

import dk.eazyit.eazyregnskab.util.CalendarUtil;
import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.resource.JasperPdfReportsResource;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.HashMap;

/**
 * @author
 */
public abstract class BaseReportPage extends LoggedInPage {


    public static final String legal_entity_name = "QUERY_LEGAL_ENTITY_NAME";
    public static final String legal_entity_id = "QUERY_LEGAL_ENTITY_ID";
    public static final String start_account = "QUERY_START_ACCOUNT_NUMBER";
    public static final String end_account = "QUERY_END_ACCOUNT_NUMBER";
    public static final String start_date = "QUERY_START_DATE";
    public static final String end_date = "QUERY_END_DATE";
    public static final String appuser = "QUERY_APPUSER";
    public static final String locale = "REPORT_LOCALE";

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
        setDefaultModel(new CompoundPropertyModel<>(reportObject = new ReportObject(
                CalendarUtil.getFirstDayInYear(),
                CalendarUtil.getLastDayInYear(),
                financeAccountService.findLowestFinanceAccountByLegalEntity(getCurrentLegalEntity()),
                financeAccountService.findHighestFinanceAccountByLegalEntity(getCurrentLegalEntity()))));
    }

    public ResourceLink getJasperPdfResourceLink(String id, String reportName, String filename) {

        JasperPdfReportsResource jr = new JasperPdfReportsResource(reportName, filename) {
            @Override
            protected HashMap<String, Object> getParameters() {
                return getParametersForReport();
            }
        };
        return new ResourceLink(id, jr);
    }

    protected HashMap<String, Object> getParametersForReport() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(legal_entity_name, getCurrentLegalEntity().getName());
        parameters.put(legal_entity_id, getCurrentLegalEntity().getId());
        parameters.put(start_account, reportObject.getAccountFrom().getAccountNumber());
        parameters.put(end_account, reportObject.getAccountTo().getAccountNumber());
        parameters.put(start_date, reportObject.getDateFrom());
        parameters.put(end_date, reportObject.getDateTo());
        parameters.put(appuser, getCurrentUser().getUsername());
        parameters.put(locale, getSession().getLocale());
        return parameters;
    }

}
