package dk.eazyit.eazyregnskab.web.app.secure.settings;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.dataprovider.VatTypeDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderNumberTextField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.VatTypeModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.panels.ActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.NumberPropertyColumn;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
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

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.vat.type", parentPage = BaseDataPage.class, subLevel = 3, topLevelPage = false)
public class VatTypesPage extends LoggedInPage {

    FinanceAccountForm form;
    AjaxFallbackDefaultDataTable dataTable;

    private static final Logger LOG = LoggerFactory.getLogger(VatTypesPage.class);

    @SpringBean
    FinanceAccountService financeAccountService;

    public VatTypesPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public VatTypesPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public VatTypesPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new FinanceAccountForm("vatTypeEdit", new CompoundPropertyModel<VatType>(new VatTypeModel(new VatType()))));

        List<IColumn<VatType, String>> columns = new ArrayList<IColumn<VatType, String>>();
        columns.add(new PropertyColumn<VatType, String>(new ResourceModel("name"), "name", "name"));
        columns.add(new NumberPropertyColumn<VatType>(new ResourceModel("percentage"), "percentage", "percentage",2,2));
        columns.add(new AbstractColumn<VatType, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<VatType>> cellItem, String componentId, IModel<VatType> rowModel) {
                cellItem.add(new VatTypeActionPanel(componentId, rowModel));
            }
        });
        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfVatTypes", columns, new VatTypeDataProvider(), 20));
    }

    private class VatTypeActionPanel extends ActionPanel<VatType> {


        /**
         * @param id    component id
         * @param model model for contact
         */
        public VatTypeActionPanel(String id, IModel<VatType> model) {
            super(id, model);
        }

        @Override
        protected List<Component> selectItem() {
            LOG.debug("Selected item " +getModelObject().toString());
            form.setDefaultModel(new CompoundPropertyModel<VatType>(new VatTypeModel(getModelObject())));
            List<Component> list = new ArrayList<Component>();
            list.add(form);
            return list;
        }

        @Override
        protected List<Component> deleteItem() {
            LOG.debug("Deleting item " +getModelObject().toString());
            form.setDefaultModel(new CompoundPropertyModel<VatType>(new VatTypeModel(getModelObject())));
            form.deleteEntity();
            List<Component> list = new ArrayList<Component>();
            list.add(dataTable);
            return list;
        }
    }

    private class FinanceAccountForm extends BaseCreateEditForm<VatType> {

        FinanceAccountForm(String id, IModel<VatType> model) {
            super(id, model);
        }

        @Override
        public void addToForm() {
            super.addToForm();
            add(new PlaceholderTextField<String>("name").setRequired(true));
            add(new PlaceholderNumberTextField<Double>("percentage").setMinimum(new Double(0)).setRequired(true));
        }

        @Override
        public void deleteEntity() {
            if (getModelObject().getId() != 0) {
                if (financeAccountService.isDeletingVatTypeAllowed(getModelObject())) {
                    financeAccountService.deleteVatType(getModelObject());
                    getSession().success(new NotificationMessage(new ResourceModel("vat.type.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                    LOG.info("Deleting vatType " + getModelObject().toString());
                } else {
                    getSession().error(new NotificationMessage(new ResourceModel("vat.type.is.in.use")).hideAfter(Duration.seconds(DURATION)));
                    LOG.info("Not able to delete vatType since its in use" + getModelObject().toString());
                }
                newEntity();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("vat.type.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
                newEntity();
            }
        }

        @Override
        public void newEntity() {
            LOG.info("Creating vatType " + getModelObject().toString());
            setDefaultModel(new CompoundPropertyModel<VatType>(new VatTypeModel(new VatType())));
            form.modelChanged();
        }

        @Override
        public void saveForm() {
            financeAccountService.saveVatType(getModelObject(), getSelectedLegalEntity().getLegalEntityModel().getObject());
            getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
            LOG.info("Saving VatType " + getModelObject().toString());
            newEntity();
        }
    }
}
