package dk.eazyit.eazyregnskab.web.components.tables.columns;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.tables.column.FinanceAccountPropertyColumn;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.ResourceModel;

import java.util.ArrayList;

/**
 * @author
 */
public class ColumnsForBalancePage extends ArrayList<IColumn<FinanceAccount, String>> {

    public ColumnsForBalancePage() {

        add(new FinanceAccountPropertyColumn<String>(new ResourceModel("BalancePage.account.number"), "accountNumber"));
        add(new FinanceAccountPropertyColumn<String>(new ResourceModel("BalancePage.account.name"), "name"));
        add(new FinanceAccountPropertyColumn<String>(new ResourceModel("BalancePage.account.sum"), "sum") {
            @Override
            public Component getHeader(String componentId) {
                return super.getHeader(componentId);
            }
        });
        add(new FinanceAccountPropertyColumn<String>(new ResourceModel("BalancePage.account.sum.compare"), "sumCompare") {
            @Override
            public Component getHeader(String componentId) {
                return super.getHeader(componentId);
            }
        });
//        add(new AbstractColumn<FinanceAccount, String>(new ResourceModel("postings")) {
//            @Override
//            public void populateItem(Item<ICellPopulator<FinanceAccount>> cellItem, String componentId, IModel<FinanceAccount> rowModel) {
//                cellItem.add( //TODO Postingsmodalwindow with export function)
//            }
//        });
    }
}
