package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import com.vaynberg.wicket.select2.Select2Choice;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinancePosting;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.FinanceAccountProvider;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinancePostingDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.models.FinancePostingModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.panels.ActionPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

        add(form = new FinancePostingForm("financePostingEdit", new FinancePostingModel(new FinancePosting())));

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

        List<IColumn<FinancePosting, String>> columns = new ArrayList<IColumn<FinancePosting, String>>();

        columns.add(new AbstractColumn<FinancePosting, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<FinancePosting>> cellItem, String componentId, IModel<FinancePosting> rowModel) {
                cellItem.add(new BookkeepingActionPanel(componentId, rowModel));
            }
        });
        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfFinancePostings", columns, new FinancePostingDataProvider(), 20));
    }

    private class BookkeepingActionPanel extends ActionPanel<FinancePosting> {

        public BookkeepingActionPanel(String id, IModel<FinancePosting> model) {
            super(id, model);
        }

        @Override
        protected List<Component> selectItem() {
            form.setModelObject(getModelObject());
            List<Component> list = new ArrayList<Component>();
            list.add(form);
            return list;
        }

        @Override
        protected List<Component> deleteItem() {
            form.setDefaultModelObject(getModelObject());
            form.deleteEntity();
            List<Component> list = new ArrayList<Component>();
            list.add(dataTable);
            return list;
        }
    }

    class FinancePostingForm extends BaseCreateEditForm<FinancePosting> {

        FinancePostingForm(String id, IModel<FinancePosting> model) {
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
            ));
            add(new Select2Choice<FinanceAccount>("financeAccount", new PropertyModel(getModelObject(), "financeAccount"), new FinanceAccountProvider()).add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    FinanceAccount financeAccount = (FinanceAccount) getFormComponent().getModelObject();
                    FinanceAccount reverse = financeAccount.getStandardReverseFinanceAccount();
                    if (reverse != null) {
                        reverseFinanceAccount.setModelObject(reverse);
                        target.add(reverseFinanceAccount);
                    }
                }
            }));
            add(reverseFinanceAccount = new Select2Choice<FinanceAccount>("reverseFinanceAccount", new PropertyModel(getModelObject(), "financeAccount"), new FinanceAccountProvider()));
            add(new PlaceholderTextField<BigDecimal>("amount"));
            add(new DropDownChoice<VatType>("vatType", financeAccountService.findAllVatTypesForLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()), new ChoiceRenderer<VatType>("name", "id")));
            add(new PlaceholderTextField<String>("text"));
        }

        @Override
        public void deleteEntity() {
//                if (legalEntityService.isDeletingAllowed(getCurrentUser().getAppUserModel().getObject(), getModelObject())) {
//
//                    legalEntityService.deleteLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
//                    getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()).get(0)));
//                    updateLegalEntitySelections();
//                    getSession().success(new NotificationMessage(new ResourceModel("legal.entity.was.deleted")).hideAfter(Duration.seconds(DURATION)));
//                } else {
//                    getSession().error(new NotificationMessage(new ResourceModel("must.be.one.legal.entity")).hideAfter(Duration.seconds(DURATION)));
//                }
        }

        @Override
        public void newEntity() {
//                LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser().getAppUserModel().getObject(),
//                        new LegalEntity(getString("new.legal.entity"), null, null, null, Country.DK, MoneyCurrency.DKK));
//                getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(newLegalEntity));
//                updateLegalEntitySelections();
//                getSession().success(new NotificationMessage(new ResourceModel("created.and.saved.new.entity")).hideAfter(Duration.seconds(DURATION)));

        }

        @Override
        public void saveForm() {
//                legalEntityService.saveLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
//                updateLegalEntitySelections();
//                getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));

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
