package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.dataprovider.FinanceAccountDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.FinanceAccountForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.FinanceAccountModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.column.ColumnsForChartsOfAccountsPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.chart.of.accounts", parentPage = BaseDataPage.class, subLevel = 1, topLevelPage = false)
public class ChartOfAccountsPage extends LoggedInPage {

    FinanceAccountForm form;
    AjaxFallbackDefaultDataTable dataTable;

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

        add(dataTable = new AjaxFallbackDefaultDataTable("chartOfAccounts", new ColumnsForChartsOfAccountsPage(form), new FinanceAccountDataProvider(), 20));
    }


    @Override
    public void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        target.add(dataTable);
    }
}
