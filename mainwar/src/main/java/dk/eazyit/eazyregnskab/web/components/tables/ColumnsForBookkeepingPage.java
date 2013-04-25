package dk.eazyit.eazyregnskab.web.components.tables;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.ResourceModel;

import java.util.ArrayList;

/**
 * @author
 */
public class ColumnsForBookkeepingPage extends ArrayList<IColumn<DraftFinancePosting, String>> {

    public ColumnsForBookkeepingPage() {

        add(new DatePropertyColumn<DraftFinancePosting>(new ResourceModel("date"), "date", "date"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("bookingNumber"), "bookingNumber", "bookingNumber"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("text"), "text", "text"));
        add(new NumberPropertyColumn<DraftFinancePosting>(new ResourceModel("amount"), "amount", "amount", 2, 2));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("financeAccount"), "financeAccount.accountNumber", "financeAccount.accountNumber"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("vatType"), "vatType.name", "vatType.name"));
        add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("finance.account.reverse"), "reverseFinanceAccount.accountNumber", "reverseFinanceAccount.accountNumber"));
        add(new CheckboxPropertyColumn<DraftFinancePosting>(new ResourceModel("chose"), "chosen"));

    }
}
