package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.web.components.validators.forms.BalanceReportFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class FinanceAccountsReportForm extends BaseReportForm {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountsReportForm.class);

    public FinanceAccountsReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        super(id, refreshOnSubmit);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public FinanceAccountsReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model, refreshOnSubmit);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
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
