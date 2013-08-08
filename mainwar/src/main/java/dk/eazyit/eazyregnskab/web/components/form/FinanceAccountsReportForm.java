package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.web.components.validators.forms.BalanceReportFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountsReportForm extends BaseReportForm {

    public FinanceAccountsReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        super(id, refreshOnSubmit);
    }

    public FinanceAccountsReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model, refreshOnSubmit);
    }

    @Override
    protected void addToForm() {
        super.addToForm();
        add(new AjaxCheckBox("hideAccountsWithOutTransactions") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                processInput();
            }
        });
        add(new BalanceReportFormValidator());
    }

    @Override
    protected void submit(AjaxRequestTarget target) {

    }
}
