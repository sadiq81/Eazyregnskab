package dk.eazyit.eazyregnskab.web.components.tables.column;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.panels.action.BookkeepingActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.columns.CheckboxPropertyColumn;
import dk.eazyit.eazyregnskab.web.components.tables.columns.DatePropertyColumn;
import dk.eazyit.eazyregnskab.web.components.tables.columns.NumberPropertyColumn;
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
public class ColumnsForBookkeepingPage extends ArrayList<IColumn<DraftFinancePosting, ?>> {

    public ColumnsForBookkeepingPage(final BaseCreateEditForm<DraftFinancePosting> form) {

        add(new DatePropertyColumn<DraftFinancePosting>(new ResourceModel("date"), "date", "date"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("bookingNumber"), "bookingNumber", "bookingNumber"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("text"), "text", "text"));
        add(new NumberPropertyColumn<DraftFinancePosting>(new ResourceModel("amount"), "amount", "amount", 2, 2));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("financeAccount"), "financeAccount.accountNumber", "financeAccount.accountNumber"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("vatType"), "vatType.name", "vatType.name"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("finance.account.reverse"), "reverseFinanceAccount.accountNumber", "reverseFinanceAccount.accountNumber"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("reverseVatType"), "vatType.name", "vatType.name"));
        add(new CheckboxPropertyColumn<DraftFinancePosting>(new ResourceModel("chose"),"chosen"));
        add(new AbstractColumn<DraftFinancePosting, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<DraftFinancePosting>> cellItem, String componentId, IModel<DraftFinancePosting> rowModel) {
                cellItem.add(new BookkeepingActionPanel(componentId, rowModel, form));
            }
        });
    }
}
