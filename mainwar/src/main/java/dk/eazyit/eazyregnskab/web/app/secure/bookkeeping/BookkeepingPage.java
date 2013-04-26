package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.web.components.choice.DailyLedgerDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.DraftFinancePostingForm;
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.ColumnsForBookkeepingPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0, topLevelPage = true, topLevel = 1)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingPage.class);

    private DailyLedgerDropDownChoice dailyLedgerChoice;
    private DraftFinancePostingForm form;

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
        add(new AjaxFallbackDefaultDataTable("chartOfFinancePostings", new ColumnsForBookkeepingPage(form), new FinancePostingDataProvider(), 20));

    }


    @Override
    public void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        addOrReplace(dailyLedgerChoice, new DailyLedgerDropDownChoice("dailyLedgerList"));
        addOrReplace(form, new DraftFinancePostingForm("financePostingEdit", new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()))));
        target.add(getPage());
    }
}
