package dk.eazyit.eazyregnskab.web.components.tables.item;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountItem extends Item<FinanceAccount> {

    public FinanceAccountItem(String id, int index, IModel<FinanceAccount> model) {
        super(id, index, model);
    }

    public FinanceAccountItem(String id, int index) {
        super(id, index);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        FinanceAccount financeAccount = getModelObject();
        switch (financeAccount.getFinanceAccountType()) {
            case HEADLINE: {
                tag.put("class", "headline");
                break;
            }
            case SUM: {
                tag.put("class", "sumfrom");
                break;
            }
        }
    }
}
