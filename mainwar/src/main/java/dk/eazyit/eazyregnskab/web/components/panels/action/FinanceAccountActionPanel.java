package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.models.FinanceAccountModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class FinanceAccountActionPanel extends ActionPanel<FinanceAccount> {

    BaseCreateEditForm<FinanceAccount> form;
    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingActionPanel.class);

    /**
     * @param id    component id
     * @param model model for contact
     */
    public FinanceAccountActionPanel(String id, IModel<FinanceAccount> model, BaseCreateEditForm<FinanceAccount> form) {
        super(id, model);
        this.form = form;
    }

    @Override
    protected BaseCreateEditForm<FinanceAccount> selectItem(FinanceAccount financeAccount) {
        LOG.debug("Selected item " + financeAccount.toString());
        form.setDefaultModel(new CompoundPropertyModel<FinanceAccount>(new FinanceAccountModel(financeAccount)));
        return form;
    }


    @Override
    protected void deleteItem(FinanceAccount financeAccount) {
        form.deleteEntity(financeAccount);
    }
}