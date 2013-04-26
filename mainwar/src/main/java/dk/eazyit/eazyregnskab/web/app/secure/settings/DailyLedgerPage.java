package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.dataprovider.DailyLedgerDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.DailyLedgerForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.column.ColumnsForDailyLedgerPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.daily.ledger", parentPage = BaseDataPage.class, subLevel = 2, topLevelPage = false)
public class DailyLedgerPage extends LoggedInPage {

    DailyLedgerForm form;
    AjaxFallbackDefaultDataTable dataTable;

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerPage.class);

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

        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfDailyLedgers", new ColumnsForDailyLedgerPage(form), new DailyLedgerDataProvider(), 20));
    }

    @Override
    public void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        target.add(dataTable);
    }
}