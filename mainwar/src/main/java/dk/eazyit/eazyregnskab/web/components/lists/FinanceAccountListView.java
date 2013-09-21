package dk.eazyit.eazyregnskab.web.components.lists;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountListView extends ListView<FinanceAccount> {

    public FinanceAccountListView(String id, IModel<? extends List<? extends FinanceAccount>> model) {
        super(id, model);
        init();
    }

    private void init() {
        setOutputMarkupId(true);
    }

    @Override
    protected void populateItem(ListItem<FinanceAccount> itemOuter) {
        FinanceAccount fa = itemOuter.getModelObject();
        itemOuter.add(new Label("BalancePage.accountNumber", fa.getAccountNumber()));
        itemOuter.add(new Label("BalancePage.accountName", fa.getName()));

        switch (fa.getFinanceAccountType()) {
            case PROFIT:
            case EXPENSE:
            case ASSET:
            case YEAR_END:
            case CURRENT_RESULT:
            case LIABILITY: {
                itemOuter.add(new Label("BalancePage.sum", fa.getSum()));
                itemOuter.add(new Label("BalancePage.sumCompare", fa.getSumCompare()));
                break;
            }
            case EMPTY_ROW: {
                itemOuter.add(new Label("BalancePage.sum", " "));
                itemOuter.add(new Label("BalancePage.sumCompare", " "));
                break;
            }
            case CATEGORY: {
                itemOuter.add(new AttributeAppender("class", "category"));
                itemOuter.add(new Label("BalancePage.sum", ""));
                itemOuter.add(new Label("BalancePage.sumCompare", ""));
                break;
            }

            case HEADLINE: {
                itemOuter.add(new AttributeAppender("class", "headline"));
                itemOuter.add(new Label("BalancePage.sum", ""));
                itemOuter.add(new Label("BalancePage.sumCompare", ""));
                break;
            }
            case BALANCE_CHECK:
            case CATEGORY_SUM:
            case SUM: {
                //TODO calculate sum
                itemOuter.add(new AttributeAppender("class", "sumfrom"));
                itemOuter.add(new Label("BalancePage.sum", fa.getSum()));
                itemOuter.add(new Label("BalancePage.sumCompare", fa.getSumCompare()));
                break;
            }
            default: {
                throw new NullPointerException("Forgot to add account type to markup");
            }
        }
    }
}
