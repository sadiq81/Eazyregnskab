package dk.eazyit.eazyregnskab.web.components.popup;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.services.PostingService;
import dk.eazyit.eazyregnskab.web.components.label.DateLabel;
import dk.eazyit.eazyregnskab.web.components.link.ToolTipLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author
 */
public class PostingPopUp extends SessionAwarePopUp<BookedFinancePosting> {

    @SpringBean
    PostingService postingService;

    public PostingPopUp(IModel<BookedFinancePosting> model) {
        super(model);

        add(new ToolTipLink("postings.reverse", "postings.reverse.help") {
            @Override
            public void onClick() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        add(new ToolTipLink("postings.copy", "postings.copy.help") {
            @Override
            public void onClick() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        add(new ToolTipLink("postings.flip", "postings.flip.help") {
            @Override
            public void onClick() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        add(new ListView<BookedFinancePosting>("postings", postingService.findBookedPostingsFromLegalEntityByBookingNumber(getCurrentLegalEntity(), (BookedFinancePosting) getDefaultModelObject())) {
            @Override
            protected void populateItem(ListItem<BookedFinancePosting> itemInner) {

                BookedFinancePosting bfp = itemInner.getModelObject();
                itemInner.add(new DateLabel("date", bfp.getDate()));
                itemInner.add(new Label("text", bfp.getText()));
                itemInner.add(new Label("vatType.name", bfp.getVatType() != null ? bfp.getVatType().getName() : ""));
                itemInner.add(new Label("amount", bfp.getAmount()));

            }
        });


    }


}
