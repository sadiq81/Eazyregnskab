package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import com.vaynberg.wicket.select2.Select2Choice;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.util.BigDecimalRangeValidator;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinanceAccountProvider;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderBigdecimalTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.panels.ActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.BigDecimalPropertyColumn;
import dk.eazyit.eazyregnskab.web.components.tables.CheckboxPropertyColumn;
import dk.eazyit.eazyregnskab.web.components.tables.DatePropertyColumn;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

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
    }

    public BookkeepingPage(IModel<?> model) {
        super(model);
    }

    public BookkeepingPage(final PageParameters parameters) {
        super(parameters);
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
                updateDailyLedger(target);
                target.add(getPage());
            }
        }));
        dailyLedgerDropDownChoice.setOutputMarkupPlaceholderTag(true);

        List<IColumn<DraftFinancePosting, String>> columns = new ArrayList<IColumn<DraftFinancePosting, String>>();

        columns.add(new DatePropertyColumn<DraftFinancePosting>(new ResourceModel("date"), "date", "date"));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("bookingNumber"), "bookingNumber", "bookingNumber"));
        columns.add(new PropertyColumn<DraftFinancePosting, String>(new ResourceModel("text"), "text", "text"));
        columns.add(new BigDecimalPropertyColumn<DraftFinancePosting>(new ResourceModel("amount"), "amount", "amount"));
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
            form.setDefaultModel(new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(getModelObject())));
            List<Component> list = new ArrayList<Component>();
            list.add(form);
            return list;
        }

        @Override
        protected List<Component> deleteItem() {
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
            add(new PlaceholderBigdecimalTextField("amount").add(new BigDecimalRangeValidator(new BigDecimal(0), null)).setRequired(true));
            add(new DropDownChoice<VatType>("vatType", financeAccountService.findAllVatTypesForLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()), new ChoiceRenderer<VatType>("name", "id")));
            add(new PlaceholderTextField<String>("text"));
            add(new PlaceholderNumberTextField<Integer>("bookingNumber").setRequired(true));
        }

        @Override
        public void deleteEntity() {

            financeAccountService.deleteFinancePosting(getModelObject());
            newEntity();
            getSession().success(new NotificationMessage(new ResourceModel("finance.posting.was.deleted")).hideAfter(Duration.seconds(DURATION)));

        }

        @Override
        public void newEntity() {
            form.setDefaultModel(new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(new DraftFinancePosting())));
        }

        @Override
        public void saveForm() {
            financeAccountService.saveDraftFinancePosting(getModelObject()
                    .setDailyLedger(getCurrentDailyLedger().getDailyLedgerModel().getObject()));
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
