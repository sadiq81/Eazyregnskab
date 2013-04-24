package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import com.vaynberg.wicket.select2.Select2Choice;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.panels.ActionPanel;
import dk.eazyit.eazyregnskab.web.components.select2providers.FinanceAccountProvider;
import dk.eazyit.eazyregnskab.web.components.tables.CheckboxPropertyColumn;
import dk.eazyit.eazyregnskab.web.components.tables.DatePropertyColumn;
import dk.eazyit.eazyregnskab.web.components.tables.NumberPropertyColumn;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0, topLevelPage = true, topLevel = 1)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingPage.class);

    @SpringBean
    FinanceAccountService financeAccountService;

    private DropDownChoice<DailyLedger> dailyLedgerDropDownChoice;
    private DailyLedgerModel dailyLedgerModel;
    private Select2Choice financeAccount;
    private Select2Choice reverseFinanceAccount;

    FinancePostingForm form;
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

        add(form = new FinancePostingForm("financePostingEdit", new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting()))));

        add(dailyLedgerDropDownChoice = new DropDownChoice<DailyLedger>("dailyLedgerList",
                dailyLedgerModel = getCurrentDailyLedger().getDailyLedgerModel(),
                financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()),
                new ChoiceRenderer<DailyLedger>("name", "id")));

        dailyLedgerDropDownChoice.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LOG.debug("Changed DailyLeger to " + getCurrentDailyLedger().getDailyLedgerModel().getObject());
                updateDailyLedger(target);
                target.add(getPage());
            }
        }));
        dailyLedgerDropDownChoice.setOutputMarkupPlaceholderTag(true);

        List<IColumn<DraftFinancePosting, String>> columns = new ArrayList<IColumn<DraftFinancePosting, String>>();

        columns.add(new DatePropertyColumn<DraftFinancePosting>(new ResourceModel("date"), "date", "date"));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("bookingNumber"), "bookingNumber", "bookingNumber"));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("text"), "text", "text"));
        columns.add(new NumberPropertyColumn<DraftFinancePosting>(new ResourceModel("amount"), "amount", "amount",2,2));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("financeAccount"), "financeAccount.accountNumber", "financeAccount.accountNumber"));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("vatType"), "vatType.name", "vatType.name"));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("finance.account.reverse"), "reverseFinanceAccount.accountNumber", "reverseFinanceAccount.accountNumber"));
        columns.add(new CheckboxPropertyColumn<DraftFinancePosting>(new ResourceModel("chose"), "chosen"));

        columns.add(new AbstractColumn<DraftFinancePosting, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<DraftFinancePosting>> cellItem, String componentId, IModel<DraftFinancePosting> rowModel) {
                cellItem.add(new BookkeepingActionPanel(componentId, rowModel));
            }
        });
        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfFinancePostings", columns, new FinancePostingDataProvider(), 20));
    }

    private class BookkeepingActionPanel extends ActionPanel<DraftFinancePosting> {

        public BookkeepingActionPanel(String id, IModel<DraftFinancePosting> model) {
            super(id, model);
        }

        @Override
        protected List<Component> selectItem() {
            LOG.debug("Selected item " +getModelObject().toString());
            form.setDefaultModel(new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(getModelObject())));
            List<Component> list = new ArrayList<Component>();
            list.add(form);
            return list;
        }

        @Override
        protected List<Component> deleteItem() {
            LOG.debug("Deleting item " +getModelObject().toString());
            financeAccountService.deleteFinancePosting(getModelObject());
            List<Component> list = new ArrayList<Component>();
            list.add(dataTable);
            return list;
        }
    }

    class FinancePostingForm extends BaseCreateEditForm<DraftFinancePosting> {

        FinancePostingForm(String id, IModel<DraftFinancePosting> model) {
            super(id, model);
        }

        @Override
        public void addToForm() {
            super.addToForm();
            add(new PlaceholderDateField("date", new DateTextFieldConfig()
                    .autoClose(true)
                    .withLanguage("da")
                    .withFormat("dd-MM-yyyy")
                    .allowKeyboardNavigation(true)
                    .showTodayButton(true)
            ).setRequired(true));
            add(financeAccount = new Select2Choice<FinanceAccount>("financeAccount"));
            financeAccount.setProvider(new FinanceAccountProvider());
            financeAccount.getSettings().setAllowClear(true);
            financeAccount.setRequired(true);
            financeAccount.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    FinanceAccount financeAccount = (FinanceAccount) getFormComponent().getModelObject();
                    FinanceAccount reverse = financeAccount.getStandardReverseFinanceAccount();
                    if (reverse != null) {
                        //TODO also set vatType
                        reverseFinanceAccount.setModelObject(reverse);
                        target.add(reverseFinanceAccount);
                    }
                }
            });
            add(reverseFinanceAccount = new Select2Choice<FinanceAccount>("reverseFinanceAccount"));
            reverseFinanceAccount.setProvider(new FinanceAccountProvider());
            reverseFinanceAccount.getSettings().setAllowClear(true);
            add(new PlaceholderNumberTextField<Double>("amount").setMinimum(new Double(0)).setRequired(true));
            add(new DropDownChoice<VatType>("vatType", financeAccountService.findAllVatTypesForLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()), new ChoiceRenderer<VatType>("name", "id")));
            add(new PlaceholderTextField<String>("text"));
            add(new PlaceholderNumberTextField<Integer>("bookingNumber").setRequired(true));
        }

        @Override
        public void deleteEntity() {

            financeAccountService.deleteFinancePosting(getModelObject());
            LOG.info("Deleting draftFinanceposting " + getModelObject().toString());
            newEntity();
            getSession().success(new NotificationMessage(new ResourceModel("finance.posting.was.deleted")).hideAfter(Duration.seconds(DURATION)));
        }

        @Override
        public void newEntity() {
            form.setDefaultModel(new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting())));
            LOG.info("Creating draftFinanceposting " + getModelObject().toString());
        }

        @Override
        public void saveForm() {

            financeAccountService.saveDraftFinancePosting(getModelObject()
                    .setDailyLedger(getCurrentDailyLedger().getDailyLedgerModel().getObject()));
            LOG.info("Saving draftFinanceposting " + getModelObject().toString());
            newEntity();
        }
    }

    @Override
    protected void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);

        DropDownChoice<DailyLedger> temp = new DropDownChoice<DailyLedger>("dailyLedgerList",
                dailyLedgerModel = getCurrentDailyLedger().getDailyLedgerModel(),
                financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()),
                new ChoiceRenderer<DailyLedger>("name", "id"));
        temp.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateDailyLedger(target);
                target.add(getPage());
                LOG.debug("Changed dailyLedger to " + getCurrentDailyLedger().getDailyLedgerModel().getObject().toString());
            }
        }));
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(dailyLedgerDropDownChoice, temp);
        temp.setParent(this);
        dailyLedgerDropDownChoice.setParent(this);
        dailyLedgerDropDownChoice = temp;

    }

    protected void updateDailyLedger(AjaxRequestTarget target) {
        target.add(dataTable);
    }
}
