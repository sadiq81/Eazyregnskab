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

    public FinanceAccountListView(String id) {
        super(id);
        init();
    }

    public FinanceAccountListView(String id, IModel<? extends List<? extends FinanceAccount>> model) {
        super(id, model);
        init();
    }

    public FinanceAccountListView(String id, List<? extends FinanceAccount> list) {
        super(id, list);
        init();
    }

    private void init(){
        setOutputMarkupId(true);
    }

    @Override
    protected void populateItem(ListItem<FinanceAccount> itemOuter) {
        FinanceAccount fa = itemOuter.getModelObject();
        itemOuter.add(new Label("accountNumber", fa.getAccountNumber()));
        itemOuter.add(new Label("accountName", fa.getName()));

        switch (fa.getFinanceAccountType()) {
            case PROFIT:
            case EXPENSE:
            case ASSET:
            case LIABILITY: {
                itemOuter.add(new Label("sum", fa.getSum()));
                break;
            }
            case HEADLINE: {
                itemOuter.add(new AttributeAppender("class", "headline"));
                itemOuter.add(new Label("sum", ""));
                break;
            }
            case SUM: {
                //TODO calculate sum
                itemOuter.add(new AttributeAppender("class", "sumfrom"));
                itemOuter.add(new Label("sum", fa.getSum()));
                break;
            }
        }
    }
}
