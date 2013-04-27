package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.BookingResult;
import dk.eazyit.eazyregnskab.domain.BookingStatus;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.BookingService;
import dk.eazyit.eazyregnskab.web.components.choice.DailyLedgerDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.DraftFinancePostingForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.column.ColumnsForBookkeepingPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0, topLevelPage = true, topLevel = 1)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingPage.class);

    private DailyLedgerDropDownChoice dailyLedgerChoice;
    private DraftFinancePostingForm form;
    private FinancePostingDataProvider financePostingDataProvider;
    private Form book;
    BookingResult bookingResult;

    @SpringBean
    BookingService bookingService;

    public BookkeepingPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public BookkeepingPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public BookkeepingPage(final PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(dailyLedgerChoice = new DailyLedgerDropDownChoice("dailyLedgerList"));
        add(form = new DraftFinancePostingForm("financePostingEdit", new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()))));
        add(new AjaxFallbackDefaultDataTable("chartOfFinancePostings", new ColumnsForBookkeepingPage(form), financePostingDataProvider = new FinancePostingDataProvider(), 20));

        add(book = new Form("book"));
        book.add(new AjaxButton("bookChosen", new ResourceModel("button.book.chosen")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                bookingService.BookChosen(getCurrentDailyLedger().getDailyLedgerModel().getObject(), bookingResult = new BookingResult());
                if (bookingResult.getBookingStatus() == BookingStatus.ERROR) {
                    getSession().error(new NotificationMessage(
                            new StringResourceModel("following.postings.did.not.balance", this, null, "",bookingResult.getListOfErrors()))
                            .hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }
        });
        book.add(new AjaxButton("bookAll", new ResourceModel("button.book.all")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                bookingService.BookAll(getCurrentDailyLedger().getDailyLedgerModel().getObject(), bookingResult = new BookingResult());
                if (bookingResult.getBookingStatus() == BookingStatus.ERROR) {
                    getSession().error(new NotificationMessage(
                            new StringResourceModel("following.postings.did.not.balance", this, null, "",bookingResult.getListOfErrors()))
                            .hideAfter(Duration.seconds(DURATION)));
                }
                target.add(getPage());
            }
        });
    }


    @Override
    public void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        addOrReplace(dailyLedgerChoice, new DailyLedgerDropDownChoice("dailyLedgerList"));
        addOrReplace(form, new DraftFinancePostingForm("financePostingEdit", new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()))));
        target.add(getPage());
    }
}
