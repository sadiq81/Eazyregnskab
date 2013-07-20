package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2ChoiceAllAccounts;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public abstract class BaseReportForm extends Form<ReportObject> {

    WebMarkupContainer refreshOnSubmit;

    public BaseReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        super(id);
        this.refreshOnSubmit = refreshOnSubmit;
        addToPage();
    }

    public BaseReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model);
        this.refreshOnSubmit = refreshOnSubmit;
        addToPage();
    }

    protected void addToPage() {

        add(new PlaceholderDateField("dateFrom", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(new PlaceholderDateField("dateTo", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(new FinanceAccountSelect2ChoiceAllAccounts("accountFrom").setRequired(true));
        add(new FinanceAccountSelect2ChoiceAllAccounts("accountTo").setRequired(true));

        add(new AjaxButton("refresh", new ResourceModel("button.refresh")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                if (refreshOnSubmit != null) target.add(refreshOnSubmit);
                submit(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                target.add(getPage().get("feedback"));
            }
        });

    }

    protected abstract void submit(AjaxRequestTarget target);
}
