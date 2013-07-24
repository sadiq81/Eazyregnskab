package dk.eazyit.eazyregnskab.web.components.label.financeaccountform;

import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.web.components.form.FinanceAccountForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class SumLabel extends Label {

    public SumLabel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed((((FinanceAccountForm) getParent()).getModelObject().getFinanceAccountType() == FinanceAccountType.SUM));
    }
}
