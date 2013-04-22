package dk.eazyit.eazyregnskab.web.app.secure.settings;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.dataprovider.DailyLedgerDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.panels.ActionPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
@MenuPosition(name = "settings.daily.ledger", parentPage = BaseDataPage.class, subLevel = 2)
public class DailyLedgerPage extends LoggedInPage {

    DailyLedgerForm form;
    AjaxFallbackDefaultDataTable dataTable;

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerPage.class);

    @SpringBean
    FinanceAccountService financeAccountService;

    public DailyLedgerPage() {
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public DailyLedgerPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public DailyLedgerPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new DailyLedgerForm("dailyLedgerEdit", new CompoundPropertyModel<DailyLedger>(new DailyLedgerModel(new DailyLedger()))));

        List<IColumn<DailyLedger, String>> columns = new ArrayList<IColumn<DailyLedger, String>>();
        columns.add(new PropertyColumn<DailyLedger, String>(new ResourceModel("name"), "name", "name"));
        columns.add(new AbstractColumn<DailyLedger, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<DailyLedger>> cellItem, String componentId, IModel<DailyLedger> rowModel) {
                cellItem.add(new DailyLedgerActionPanel(componentId, rowModel));
            }
        });
        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfDailyLedgers", columns, new DailyLedgerDataProvider(), 20));
    }

    private class DailyLedgerActionPanel extends ActionPanel<DailyLedger> {

        /**
         * @param id    component id
         * @param model model for contact
         */
        public DailyLedgerActionPanel(String id, IModel<DailyLedger> model) {
            super(id, model);
        }

        @Override
        protected List<Component> selectItem() {
            LOG.debug("Selected item " +getModelObject().toString());
            form.setDefaultModel(new CompoundPropertyModel<DailyLedger>(new DailyLedgerModel(getModelObject())));
            List<Component> list = new ArrayList<Component>();
            list.add(form);
            return list;
        }

        @Override
        protected List<Component> deleteItem() {
            LOG.debug("Deleting item " +getModelObject().toString());
            form.setDefaultModel(new CompoundPropertyModel<DailyLedger>(new DailyLedgerModel(getModelObject())));
            form.deleteEntity();
            List<Component> list = new ArrayList<Component>();
            list.add(dataTable);
            return list;
        }
    }

    private class DailyLedgerForm extends BaseCreateEditForm<DailyLedger> {

        DailyLedgerForm(String id, IModel<DailyLedger> model) {
            super(id, model);
        }

        @Override
        public void addToForm() {
            super.addToForm();
            add(new PlaceholderTextField<String>("name").setRequired(true));
        }

        @Override
        public void deleteEntity() {
            if (getModelObject().getId() != 0) {
                if (financeAccountService.isDeletingDailyLedgerAllowed(getModelObject(), getSelectedLegalEntity().getLegalEntityModel().getObject())) {
                    financeAccountService.deleteDailyLedger(getModelObject());
                    getCurrentDailyLedger().setDailyLedgerModel(new DailyLedgerModel(financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()).get(0)));
                    getSession().success(new NotificationMessage(new ResourceModel("daily.ledger.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                    LOG.info("Deleting Dailyledger " + getModelObject().toString());
                } else {
                    getSession().error(new NotificationMessage(new ResourceModel("daily.ledger.is.in.use")).hideAfter(Duration.seconds(DURATION)));
                    LOG.info("Not able to delete Dailyledger since its in use " + getModelObject().toString());
                }
                newEntity();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("daily.ledger.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
                newEntity();
            }
        }

        @Override
        public void newEntity() {
            setDefaultModel(new CompoundPropertyModel<DailyLedger>(new DailyLedger(getString("new.ledger"),getSelectedLegalEntity().getLegalEntityModel().getObject())));
            LOG.info("Creating dailyLedger " + getModelObject().toString());
        }

        @Override
        public void saveForm() {
            financeAccountService.saveDailyLedger(getModelObject(), getSelectedLegalEntity().getLegalEntityModel().getObject());
            getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
            LOG.info("Saving dailyLedger " + getModelObject().toString());
            newEntity();
        }
    }

    @Override
    protected void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        target.add(dataTable);

    }
}