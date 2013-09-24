package dk.eazyit.eazyregnskab.web.components.label.financeaccountform;

import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.web.components.form.FinanceAccountForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class SumLabel extends Label {

    private static final Logger LOG = LoggerFactory.getLogger(SumLabel.class);

    public SumLabel(String id, IModel<?> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed((((FinanceAccountForm) getParent()).getModelObject().getFinanceAccountType() == FinanceAccountType.SUM));
    }
}
