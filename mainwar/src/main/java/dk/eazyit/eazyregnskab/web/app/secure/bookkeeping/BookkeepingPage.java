package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.web.components.choice.DailyLedgerChooser;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.DraftFinancePostingForm;
import dk.eazyit.eazyregnskab.web.components.form.SaveDailyLedgerForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0, topLevelPage = true, topLevel = 1)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingPage.class);
    boolean selected = false;

    private DailyLedgerChooser dailyLedgerChoice;
    private DraftFinancePostingForm form;
    private FinancePostingDataProvider financePostingDataProvider;

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

        add(dailyLedgerChoice = new DailyLedgerChooser("dailyLedgerList"));
        add(form = new DraftFinancePostingForm("financePostingEdit", new CompoundPropertyModel<>(new DraftFinancePostingModel(new DraftFinancePosting(getCurrentDailyLedger().getNextBookingNumber())))));
//        add(new ExportableDataTable("chartOfFinancePostings", new ColumnsForBookkeepingPage(form), financePostingDataProvider = new FinancePostingDataProvider(), 20, "BookkeepingPage.datatable.export-file-name", new float[]{70, 70, 90, 50, 65, 65, 50, 65}) {
//            @Override
//            public String getTitle() {
//                return new StringResourceModel("BookkeepingPage.report.title", this, new Model<>(getCurrentLegalEntity())).getString();
//            }
//        });
        add(new SaveDailyLedgerForm("book"));
    }

    @Override
    protected void configureComponents() {
    }
}
