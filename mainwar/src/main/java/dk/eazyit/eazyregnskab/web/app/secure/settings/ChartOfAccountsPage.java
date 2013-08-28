package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinanceAccountDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.FinanceAccountForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.FinanceAccountModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.columns.ColumnsForChartsOfAccountsPage;
import dk.eazyit.eazyregnskab.web.components.tables.tables.ExportableDataTable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.chart.of.accounts", parentPage = BaseDataPage.class, subLevel = 1, topLevelPage = false)
public class ChartOfAccountsPage extends LoggedInPage {

    FinanceAccountForm form;
    ExportableDataTable dataTable;

    private static final Logger LOG = LoggerFactory.getLogger(ChartOfAccountsPage.class);

    public ChartOfAccountsPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public ChartOfAccountsPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public ChartOfAccountsPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new FinanceAccountForm("financeAccountEdit", new FinanceAccountModel(new FinanceAccount())));
        add(dataTable = new ExportableDataTable("chartOfAccounts", new ColumnsForChartsOfAccountsPage(form), new FinanceAccountDataProvider(), getCurrentUser().getItemsPerPage(), "ChartOfAccountsPage.datatable.export-file-name", new float[]{150, 110, 105, 100, 80}) {
            @Override
            public String getTitle() {
                return new StringResourceModel("ChartOfAccountsPage.report.title", this, new Model<>(getCurrentLegalEntity())).getString();
            }
        });

    }

    @Override
    protected void configureComponents() {

    }


}
