package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.services.BookingService;
import dk.eazyit.eazyregnskab.services.PostingService;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.web.components.button.AjaxLoadingButton;
import dk.eazyit.eazyregnskab.web.components.modal.AreYouSureModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
    @SpringBean
    PostingService postingService;

    AreYouSureModal areYouSureModal;
    AjaxLoadingButton bookAll;
    AjaxLoadingButton clear;

    protected final static int DURATION = 15;

    public SaveDailyLedgerForm(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        //        add(new AjaxLoadingButton("bookChosen", new ResourceModel("button.book.chosen")) {
        //            @Override
        //            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        //                super.onSubmit(target, form);
        //                bookPostings(false);
        //                target.add(getPage());
        //            }
        //        });
        add(bookAll = new AjaxLoadingButton("bookAll", new ResourceModel("BookkeepingPage.button.book.all")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                bookPostings(true);
                target.add(getPage());
            }
        });

        add(clear = new AjaxLoadingButton("clear.daily.ledger", new ResourceModel("BookkeepingPage.button.clear.daily.ledger")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                areYouSureModal.show(target);
            }
        });

        add(areYouSureModal = new AreYouSureModal("are.you.sure", new StringResourceModel("BookkeepingPage.do.you.want.clear.daily.ledger", getPage(), null).getObject()) {
            @Override
            protected void onConfirm(AjaxRequestTarget target) {
                postingService.clearDailyLedger(getCurrentDailyLedger());
                target.add(getPage());
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                target.add(clear);
            }
        });
    }

    private void bookPostings(boolean bookAll) {

        BookingResult bookingResult;

        bookingService.BookDailyLedger(getCurrentDailyLedger(), bookingResult = new BookingResult(), bookAll);

        if (bookingResult.getBookingStatus() == BookingStatus.ERROR) {

            if (bookingResult.getNotInBalance().size() > 0) {
                getSession().error(new NotificationMessage(
                        new StringResourceModel("BookkeepingPage.following.postings.did.not.balance", this, null, "", bookingResult.getListOfNotInBalance()))
                        .hideAfter(Duration.seconds(DURATION)));
            }

            if (bookingResult.getNotInOpenFiscalYear().size() > 0) {
                getSession().error(new NotificationMessage(
                        new StringResourceModel("BookkeepingPage.following.postings.where.not.in.open.fiscal.year", this, null, "", bookingResult.getListOfNotInFiscalYear()))
                        .hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().success(new NotificationMessage(
                    new StringResourceModel("BookkeepingPage.finance.postings.where.booked", this, null, ""))
                    .hideAfter(Duration.seconds(DURATION)));
        }
    }


    protected AppUser getCurrentUser() {
        return ((EazyregnskabSesssion) getSession()).getCurrentUser();
    }

    protected LegalEntity getCurrentLegalEntity() {
        return ((EazyregnskabSesssion) getSession()).getCurrentLegalEntity();
    }

    protected void setCurrentLegalEntity(LegalEntity legalEntity) {
        ((EazyregnskabSesssion) getSession()).setCurrentLegalEntity(legalEntity);
    }

    public DailyLedger getCurrentDailyLedger() {
        return ((EazyregnskabSesssion) getSession()).getCurrentDailyLedger();
    }

    protected void setCurrentDailyLedger(DailyLedger dailyLedger) {
        ((EazyregnskabSesssion) getSession()).setCurrentDailyLedger(dailyLedger);
    }

}
