package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.choice.DailyLedgerDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.choice.FinanceAccountSelect2Choice;
import dk.eazyit.eazyregnskab.web.components.choice.VatTypeDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.ColumnsForBookkeepingPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0, topLevelPage = true, topLevel = 1)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingPage.class);

    @SpringBean
    FinanceAccountService financeAccountService;

    private DailyLedgerDropDownChoice dailyLedgerChoice;
    private FinanceAccountSelect2Choice reverseFinanceAccountChoice;
    private VatTypeDropDownChoice vatTypeChoice;

    DraftFinancePostingForm form;
    AjaxFallbackDefaultDataTable dataTable;

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
        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfFinancePostings", new ColumnsForBookkeepingPage(form), new FinancePostingDataProvider(), 20));
    }

    class DraftFinancePostingForm extends BaseCreateEditForm<DraftFinancePosting> {

        DraftFinancePostingForm(String id, IModel<DraftFinancePosting> model) {
            super(id, model);
        }

        @Override
        public void addToForm() {
            super.addToForm();

            add(new PlaceholderDateField("date", new DateTextFieldConfig().autoClose(true).withLanguage("da").withFormat("dd-MM-yyyy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
            add(reverseFinanceAccountChoice = new FinanceAccountSelect2Choice("reverseFinanceAccount"));
            add(vatTypeChoice = new VatTypeDropDownChoice("vatType"));
            add(new FinanceAccountSelect2Choice("financeAccount", reverseFinanceAccountChoice, vatTypeChoice));
            add(new PlaceholderNumberTextField<Double>("amount").setMinimum(new Double(0)).setRequired(true));
            add(new PlaceholderTextField<String>("text"));
            add(new PlaceholderNumberTextField<Integer>("bookingNumber").setRequired(true));
        }

        @Override
        public void deleteEntity(DraftFinancePosting draftFinancePosting) {
            super.deleteEntity(draftFinancePosting);
            financeAccountService.deleteFinancePosting(draftFinancePosting);
            getSession().success(new NotificationMessage(new ResourceModel("finance.posting.was.deleted")).hideAfter(Duration.seconds(DURATION)));
        }

        @Override
        public CompoundPropertyModel<DraftFinancePosting> newEntity() {
            return new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()));
        }

        @Override
        public void saveForm(DraftFinancePosting draftFinancePosting) {
            super.saveForm(null);
            financeAccountService.saveDraftFinancePosting(draftFinancePosting.setDailyLedger(getCurrentDailyLedger().getDailyLedgerModel().getObject()));
        }
    }

    @Override
    protected void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        addOrReplace(dailyLedgerChoice, new DailyLedgerDropDownChoice("dailyLedgerList"));
        addOrReplace(form, new DraftFinancePostingForm("financePostingEdit", new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()))));
        target.add(getPage());
    }
}
