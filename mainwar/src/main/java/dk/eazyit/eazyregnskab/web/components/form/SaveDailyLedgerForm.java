package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.BookingResult;
import dk.eazyit.eazyregnskab.domain.BookingStatus;
import dk.eazyit.eazyregnskab.services.BookingService;
import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class SaveDailyLedgerForm extends Form {

    @SpringBean
    BookingService bookingService;
    BookingResult bookingResult;

    protected final static int DURATION = 5;

    public SaveDailyLedgerForm(String id) {
        super(id);
        init();
    }

    private void init() {
        add(new AjaxButton("bookChosen", new ResourceModel("button.book.chosen")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                bookingService.BookChosen(getCurrentDailyLedger().getDailyLedgerModel().getObject(), bookingResult = new BookingResult());
                if (bookingResult.getBookingStatus() == BookingStatus.ERROR) {
                    getSession().error(new NotificationMessage(
                            new StringResourceModel("following.postings.did.not.balance", this, null, "", bookingResult.getListOfErrors()))
                            .hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }
        });
        add(new AjaxButton("bookAll", new ResourceModel("button.book.all")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                bookingService.BookAll(getCurrentDailyLedger().getDailyLedgerModel().getObject(), bookingResult = new BookingResult());
                if (bookingResult.getBookingStatus() == BookingStatus.ERROR) {
                    getSession().error(new NotificationMessage(
                            new StringResourceModel("following.postings.did.not.balance", this, null, "", bookingResult.getListOfErrors()))
                            .hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }
        });
    }

    public CurrentDailyLedger getCurrentDailyLedger() {
        return (CurrentDailyLedger) getSession().getAttribute(CurrentDailyLedger.ATTRIBUTE_NAME);
    }
}
