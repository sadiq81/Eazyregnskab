package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.web.components.lists.FinanceAccountListView;
import dk.eazyit.eazyregnskab.web.components.models.lists.FinanceAccountListModelWithSum;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class BalancePanel extends SessionAwarePanel {

    public BalancePanel(String id) {
        super(id);
    }

    public BalancePanel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void addToPage() {
        setOutputMarkupId(true);

        add(new FinanceAccountListView("financeAccounts", new FinanceAccountListModelWithSum(new CompoundPropertyModel(getDefaultModel()))));
    }


}
