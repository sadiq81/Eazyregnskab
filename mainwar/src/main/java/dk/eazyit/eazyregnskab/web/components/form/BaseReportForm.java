package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.button.AjaxLoadingButton;
import dk.eazyit.eazyregnskab.web.components.button.LoadingButton;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2ChoiceAllAccounts;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author
 */
public abstract class BaseReportForm extends BaseForm<ReportObject> {

    private static final Logger LOG = LoggerFactory.getLogger(LoadingButton.class);

    public static final String start_account = "QUERY_START_ACCOUNT_NUMBER";
    public static final String end_account = "QUERY_END_ACCOUNT_NUMBER";
    public static final String start_date = "QUERY_START_DATE";
    public static final String end_date = "QUERY_END_DATE";
    public static final String start_date_compare = "QUERY_START_DATE_COMPARE";
    public static final String end_date_compare = "QUERY_END_DATE_COMPARE";
    public static final String compare_type = "COMPARE_TYPE";


    WebMarkupContainer refreshOnSubmit;
    AjaxButton plus;
    AjaxButton minus;
    BaseReportForm self;


    public BaseReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        this(id, null, refreshOnSubmit);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public BaseReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
        this.refreshOnSubmit = refreshOnSubmit;
        self = this;
        addToForm();
    }

    protected void addToForm() {

        getSession().setAttribute(ReportObject.ATTRIBUTE_NAME, self.getModelObject());

        add(new PlaceholderDateField("dateFrom", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(new PlaceholderDateField("dateTo", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(new FinanceAccountSelect2ChoiceAllAccounts("accountFrom").setRequired(true));
        add(new FinanceAccountSelect2ChoiceAllAccounts("accountTo").setRequired(true));

        add(new AjaxCheckBox("hideAccountsWithOutSum") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                processInput();
            }
        });

        add(new AjaxLoadingButton("refresh", new ResourceModel("button.refresh")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                LOG.debug("refreshed reportform " + getModelObject());
                getSession().setAttribute(ReportObject.ATTRIBUTE_NAME, self.getModelObject());
                target.add(this);
                self.getModelObject().setSubmitHasBeenPressed(true);
                if (refreshOnSubmit != null) target.add(refreshOnSubmit);
                submit(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(this);
                target.add(getPage().get("feedback"));
            }
        });

        add(plus = new AjaxLoadingButton("plusOneYear") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                ReportObject reportObject = ((ReportObject) form.getModelObject());
                reportObject.setDateFrom(CalendarUtil.add(reportObject.getDateFrom(), 1, 0, 0));
                reportObject.setDateTo(CalendarUtil.add(reportObject.getDateTo(), 1, 0, 0));
                target.add(getPage());

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(getPage().get("feedback"));
            }
        });
        addToolTipToComponent(plus, "plus.one.year");

        add(minus = new AjaxLoadingButton("minusOneYear") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                ReportObject reportObject = ((ReportObject) form.getModelObject());
                reportObject.setDateFrom(CalendarUtil.add(reportObject.getDateFrom(), -1, 0, 0));
                reportObject.setDateTo(CalendarUtil.add(reportObject.getDateTo(), -1, 0, 0));
                target.add(getPage());
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(getPage().get("feedback"));
            }
        });
        addToolTipToComponent(minus, "minus.one.year");
    }

    protected void addReports() {
        add(getJasperPdfResourceLink("exportPdf", getJasperReportName(), getString(getJasperReportName() + ".pdf")));
        add(getJasperXlsResourceLink("exportXls", getJasperReportName(), getString(getJasperReportName() + ".xls")));
    }


    protected HashMap<String, Object> getFormParametersForReport() {
        HashMap<String, Object> parameters = super.getFormParametersForReport();
        parameters.put(start_account, getModelObject().getAccountFrom().getAccountNumber());
        parameters.put(end_account, getModelObject().getAccountTo().getAccountNumber());
        parameters.put(start_date, getModelObject().getDateFrom());
        parameters.put(end_date, getModelObject().getDateTo());
        parameters.put(start_date_compare, getModelObject().getDateFromCompare());
        parameters.put(end_date_compare, getModelObject().getDateToCompare());
        parameters.put(compare_type, getString("ReportCompareType." + (getModelObject().getReportCompareType().name())));
        return parameters;
    }

    protected abstract void submit(AjaxRequestTarget target);

}
