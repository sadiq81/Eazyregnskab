package dk.eazyit.eazyregnskab.web.components.tables;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.panels.action.FinanceAccountActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.columns.EnumPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.ArrayList;

/**
 * @author
 */
public class ColumnsForChartsOfAccountsPage extends ArrayList<IColumn<FinanceAccount, String>> {

    public ColumnsForChartsOfAccountsPage(final BaseCreateEditForm<FinanceAccount> form) {

        add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("name"), "name", "name"));
        add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("accountNumber"), "accountNumber", "accountNumber"));
        add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("vatType"), "vatType.percentage", "vatType.name"));
        add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("standard.reverse.finance.account"), "standardReverseFinanceAccount.name", "standardReverseFinanceAccount.name"));
        add(new EnumPropertyColumn<FinanceAccount>(new ResourceModel("financeAccountType"), "financeAccountType", "financeAccountType"));
        add(new AbstractColumn<FinanceAccount, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<FinanceAccount>> cellItem, String componentId, IModel<FinanceAccount> rowModel) {
                cellItem.add(new FinanceAccountActionPanel(componentId, rowModel, form));
            }
        });
    }
}
