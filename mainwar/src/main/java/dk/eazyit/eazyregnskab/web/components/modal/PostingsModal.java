package dk.eazyit.eazyregnskab.web.components.modal;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.services.BookingService;
import dk.eazyit.eazyregnskab.services.DailyLedgerService;
import dk.eazyit.eazyregnskab.services.PostingService;
import dk.eazyit.eazyregnskab.web.components.label.DateLabel;
import dk.eazyit.eazyregnskab.web.components.link.AjaxToolTipLink;
import dk.eazyit.eazyregnskab.web.components.lists.EazyRefreshingView;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.util.Collections;
import java.util.List;

/**
 * @author
 */
public class PostingsModal extends SessionAwareModal<BookedFinancePosting> {

    @SpringBean
    PostingService postingService;
    @SpringBean
    BookingService bookingService;
    @SpringBean
    DailyLedgerService dailyLedgerService;

    public BookedFinancePosting item;
    List<BookedFinancePosting> postingList;

    ModalWindow self;

    public PostingsModal(String id) {
        super(id);
        self = this;
        setContent(new Page(getContentId()));

        setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                target.add(getPage().get("feedback"));
            }
        });
        setInitialWidth(800);
    }

    public PostingsModal setItem(BookedFinancePosting item) {
        this.item = item;
        return this;
    }

    class Page extends GenericPanel<BookedFinancePosting> {

        Page(String id) {
            super(id);

            add(new AjaxToolTipLink("postings.regret", "postings.regret.help") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    bookingService.regretPostings(postingList, dailyLedgerService.findDailyLedgerByLegalEntitySubList(getCurrentLegalEntity(), 0, 1).get(0));
                    getSession().success(new NotificationMessage(new ResourceModel("postings.have.been.added.to.daily.ledger")).hideAfter(Duration.seconds(5)));
                    self.close(target);
                }
            });
            add(new AjaxToolTipLink("postings.copy", "postings.copy.help") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    bookingService.copyPostings(postingList, dailyLedgerService.findDailyLedgerByLegalEntitySubList(getCurrentLegalEntity(), 0, 1).get(0));
                    getSession().success(new NotificationMessage(new ResourceModel("postings.have.been.added.to.daily.ledger")).hideAfter(Duration.seconds(5)));
                    self.close(target);
                }
            });
            add(new AjaxToolTipLink("postings.flip", "postings.flip.help") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    bookingService.flipPostings(postingList, dailyLedgerService.findDailyLedgerByLegalEntitySubList(getCurrentLegalEntity(), 0, 1).get(0));
                    getSession().success(new NotificationMessage(new ResourceModel("postings.have.been.added.to.daily.ledger")).hideAfter(Duration.seconds(5)));
                    self.close(target);
                }
            });

            add(new EazyRefreshingView<BookedFinancePosting>("postings") {

                @Override
                protected List<BookedFinancePosting> getListOfItems() {
                    return item != null ? postingList = postingService.findBookedPostingsFromLegalEntityByBookingNumber(getCurrentLegalEntity(), item) : Collections.EMPTY_LIST;
                }

                @Override
                protected void populateItem(Item<BookedFinancePosting> itemInner) {

                    BookedFinancePosting bfp = itemInner.getModelObject();
                    itemInner.add(new DateLabel("date", bfp.getDate()));
                    itemInner.add(new Label("text", bfp.getText()));
                    itemInner.add(addToolTipToComponent(new Label("account", bfp.getFinanceAccount().getAccountNumber()), bfp.getFinanceAccount().getName()));
                    itemInner.add(new Label("vatType.name", bfp.getVatType() != null ? bfp.getVatType().getName() : ""));
                    itemInner.add(new Label("amount", bfp.getAmount()));

                }
            });
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            super.renderHead(response);
            Bootstrap.renderHead(response);
        }
    }


}
