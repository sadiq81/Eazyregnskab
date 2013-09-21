package dk.eazyit.eazyregnskab.web.components.lists;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.app.secure.reports.FinanceAccountsPage;
import dk.eazyit.eazyregnskab.web.components.label.DateLabel;
import dk.eazyit.eazyregnskab.web.components.link.AjaxToolTipLink;
import dk.eazyit.eazyregnskab.web.components.modal.PostingsModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class BookedFinancePostingListView extends ListView<FinanceAccount> {

    BookedFinancePosting last = null;

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
        itemOuter.add(new Label("FinanceAccountsPage.accountNumber", fa.getAccountNumber()));
        itemOuter.add(new Label("FinanceAccountsPage.accountName", fa.getName()));


        itemOuter.add(new ListView<BookedFinancePosting>("FinanceAccountsPage.postings", fa.getBookedFinancePostingList()) {

            @Override
            protected void populateItem(final ListItem<BookedFinancePosting> itemInner) {

                final BookedFinancePosting bfp = itemInner.getModelObject();
                last = bfp;
                itemInner.add(new DateLabel("FinanceAccountsPage.date", bfp.getDate()));
                itemInner.add(new Label("FinanceAccountsPage.bookingNumber", bfp.getBookingNumber()));
                itemInner.add(new Label("FinanceAccountsPage.text", bfp.getText()));
                itemInner.add(new Label("FinanceAccountsPage.vatType.name", bfp.getVatPosting() != null ? bfp.getVatPosting().getVatType().getName() : ""));
                itemInner.add(new Label("FinanceAccountsPage.amount", bfp.getAmount()));
                itemInner.add(new Label("FinanceAccountsPage.sum", bfp.getSum()));
                itemInner.add(new AjaxToolTipLink("FinanceAccountsPage.postings", "tooltip.postings") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {

                        ((PostingsModal) ((FinanceAccountsPage) getPage()).postingsModal.setItem(bfp)).show(target);

                    }
                });
            }
        });

        itemOuter.add(new Label("FinanceAccountsPage.sum", last != null ? last.getSum() : ""));

    }
}
