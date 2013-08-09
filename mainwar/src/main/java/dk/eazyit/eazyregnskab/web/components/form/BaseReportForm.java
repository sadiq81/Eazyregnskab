package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.button.AjaxLoadingButton;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2ChoiceAllAccounts;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public abstract class BaseReportForm extends Form<ReportObject> {

    WebMarkupContainer refreshOnSubmit;
    AjaxButton plus;
    AjaxButton minus;
    BaseReportForm self;

    public BaseReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        super(id);
        this.refreshOnSubmit = refreshOnSubmit;
        self = this;
        addToForm();
    }

    public BaseReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model);
        this.refreshOnSubmit = refreshOnSubmit;
        self = this;
        addToForm();
    }

    protected void addToForm() {

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

    protected abstract void submit(AjaxRequestTarget target);

    protected void addToolTipToComponent(Component component, String resourceText) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }
}
