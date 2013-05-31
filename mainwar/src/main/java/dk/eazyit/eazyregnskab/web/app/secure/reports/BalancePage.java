package dk.eazyit.eazyregnskab.web.app.secure.reports;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.models.lists.FinanceAccountListModelWithSum;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
@MenuPosition(name = "reports.balance", parentPage = BalancePage.class, subLevel = 0, topLevelPage = true, topLevel = 2)
public class BalancePage extends LoggedInPage {

    private static final Logger LOG = LoggerFactory.getLogger(BalancePage.class);

    public BalancePage() {
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public BalancePage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public BalancePage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(new ListView<FinanceAccount>("financeAccounts", new FinanceAccountListModelWithSum()) {
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
                        itemOuter.add(new AttributeAppender("class","headline"));
                        itemOuter.add(new Label("sum", ""));
                        break;
                    }
                    case SUMFROM: {
                        //TODO calculate sum
                        itemOuter.add(new Label("sum", ""));
                        break;
                    }
                }
            }
        });
    }
}
