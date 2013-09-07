package dk.eazyit.eazyregnskab.web.app.secure.reports;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.dataprovider.BalanceDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.BalanceReportForm;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.BaseReportPage;
import dk.eazyit.eazyregnskab.web.components.tables.columns.ColumnsForBalancePage;
import dk.eazyit.eazyregnskab.web.components.tables.tables.EazyDataTable;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
@MenuPosition(name = "reports.balance", parentPage = BalancePage.class, subLevel = 0, topLevelPage = true, topLevel = 2)
public class BalancePage extends BaseReportPage {

    private static final Logger LOG = LoggerFactory.getLogger(BalancePage.class);

    EazyDataTable dataTable;

    public BalancePage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public BalancePage(IModel<ReportObject> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public BalancePage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(dataTable = new EazyDataTable("balanceDataTable", new ColumnsForBalancePage(), new BalanceDataProvider(new CompoundPropertyModel(getDefaultModel()))));
        add(new BalanceReportForm("filters", new CompoundPropertyModel(getDefaultModel()), dataTable));

    }

    @Override
    protected void configureComponents() {

    }

}