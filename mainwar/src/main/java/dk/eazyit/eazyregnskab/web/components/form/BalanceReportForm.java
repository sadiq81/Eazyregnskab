package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.web.components.validators.forms.BalanceReportFormValidator;import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class BalanceReportForm extends BaseReportForm{

    public BalanceReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        super(id, refreshOnSubmit);
    }

    public BalanceReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model, refreshOnSubmit);
    }

    @Override
    protected void addToForm() {
        super.addToForm();

        add(new BalanceReportFormValidator());
    }

    @Override
    protected void submit(AjaxRequestTarget target) {

    }
}
