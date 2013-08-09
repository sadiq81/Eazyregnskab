package dk.eazyit.eazyregnskab.web.components.lists;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.label.DateLabel;
import dk.eazyit.eazyregnskab.web.components.link.ToolTipLink;
import dk.eazyit.eazyregnskab.web.components.popup.EazyRegnskabPopUpSettings;
import dk.eazyit.eazyregnskab.web.components.popup.PostingPopUp;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class BookedFinancePostingListView extends ListView<FinanceAccount> {

    public BookedFinancePostingListView(String id) {
        super(id);
        init();
    }

    public BookedFinancePostingListView(String id, IModel<? extends List<? extends FinanceAccount>> model) {
        super(id, model);
        init();
    }

    public BookedFinancePostingListView(String id, List<? extends FinanceAccount> list) {
        super(id, list);
        init();
    }

    private void init() {
        setOutputMarkupId(true);
    }

    @Override
    protected void populateItem(ListItem<FinanceAccount> itemOuter) {

        FinanceAccount fa = itemOuter.getModelObject();
        itemOuter.add(new Label("accountNumber", fa.getAccountNumber()));
        itemOuter.add(new Label("accountName", fa.getName()));

        itemOuter.add(new ListView<BookedFinancePosting>("postings", fa.getBookedFinancePostingList()) {

            @Override
            protected void populateItem(final ListItem<BookedFinancePosting> itemInner) {

                BookedFinancePosting bfp = itemInner.getModelObject();
                itemInner.add(new DateLabel("date", bfp.getDate()));
                itemInner.add(new Label("bookingNumber", bfp.getBookingNumber()));
                itemInner.add(new Label("text", bfp.getText()));
                itemInner.add(new Label("vatType.name", bfp.getVatType() != null ? bfp.getVatType().getName() : ""));
                itemInner.add(new Label("amount", bfp.getAmount()));
                itemInner.add(new Label("sum", bfp.getSum()));
                itemInner.add(new ToolTipLink("postings", "tooltip.postings") {
                    @Override
                    public void onClick() {
                        setResponsePage(new PostingPopUp(itemInner.getModel()));
                    }
                }.setPopupSettings(new EazyRegnskabPopUpSettings(getString("tooltip.postings"))));
            }
        });


    }
}
