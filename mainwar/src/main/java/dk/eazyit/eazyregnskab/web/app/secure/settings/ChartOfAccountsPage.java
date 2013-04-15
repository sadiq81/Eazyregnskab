package dk.eazyit.eazyregnskab.web.app.secure.settings;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinanceAccountDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.FinanceAccountModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.panels.ActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.EnumPropertyColumn;
import org.apache.wicket.Component;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.chart.of.accounts", parentPage = BaseDataPage.class, subLevel = 1)
public class ChartOfAccountsPage extends LoggedInPage {

    FinanceAccountForm form;
    AjaxFallbackDefaultDataTable dataTable;

    @SpringBean
    FinanceAccountService financeAccountService;

    public ChartOfAccountsPage() {
        super();
    }

    public ChartOfAccountsPage(IModel<?> model) {
        super(model);
    }

    public ChartOfAccountsPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new FinanceAccountForm("financeAccountEdit", new FinanceAccountModel(new FinanceAccount())));

        List<IColumn<FinanceAccount, String>> columns = new ArrayList<IColumn<FinanceAccount, String>>();
        columns.add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("name"), "name", "name"));
        columns.add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("accountNumber"), "accountNumber", "accountNumber"));
        columns.add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("vatType"), "vatType.percentage", "vatType.name"));
        columns.add(new EnumPropertyColumn<FinanceAccount>(new ResourceModel("financeAccountType"), "financeAccountType", "financeAccountType"));
        columns.add(new AbstractColumn<FinanceAccount, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<FinanceAccount>> cellItem, String componentId, IModel<FinanceAccount> rowModel) {
                cellItem.add(new FinanceAccountActionPanel(componentId, rowModel));
            }
        });
        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfAccounts", columns, new FinanceAccountDataProvider(this), 20));
    }

    private class FinanceAccountActionPanel extends ActionPanel<FinanceAccount> {


        /**
         * @param id    component id
         * @param model model for contact
         */
        public FinanceAccountActionPanel(String id, IModel<FinanceAccount> model) {
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

    private class FinanceAccountForm extends BaseCreateEditForm<FinanceAccount> {

        FinanceAccountForm(String id, IModel<FinanceAccount> model) {
            super(id, model);
        }

        @Override
        public void addToForm() {
            super.addToForm();
            add(new PlaceholderTextField<String>("name"));
            add(new PlaceholderTextField<String>("accountNumber"));
            add(new DropDownChoice<VatType>("vatType", financeAccountService.findAllVatTypesForLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()), new ChoiceRenderer<VatType>("name", "id")));
            add(new EnumDropDownChoice<FinanceAccountType>("financeAccountType", Arrays.asList(FinanceAccountType.values())));
        }

        @Override
        public void deleteEntity() {
            if (getModelObject().getId() != 0) {
                if (financeAccountService.isDeletingAllowed(getModelObject())) {
                    financeAccountService.deleteFinanceAccount(getModelObject());
                    getSession().success(new NotificationMessage(new ResourceModel("finance.account.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                } else {
                    getSession().error(new NotificationMessage(new ResourceModel("finance.account.is.in.use")).hideAfter(Duration.seconds(DURATION)));
                }
                newEntity();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("finance.account.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
                newEntity();
            }
        }

        @Override
        public void newEntity() {
            clearInput();
            FinanceAccount financeAccount = new FinanceAccount();
            setDefaultModel(new CompoundPropertyModel<FinanceAccount>(financeAccount));
            form.modelChanged();
        }

        @Override
        public void saveForm() {
            financeAccountService.saveFinanceAccount(getModelObject(), getSelectedLegalEntity().getLegalEntityModel().getObject());
            getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
            newEntity();
        }
    }
}
