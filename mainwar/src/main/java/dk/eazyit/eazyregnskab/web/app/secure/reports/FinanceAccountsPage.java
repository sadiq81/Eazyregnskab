package dk.eazyit.eazyregnskab.web.app.secure.reports;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.form.FinanceAccountsReportForm;
import dk.eazyit.eazyregnskab.web.components.modal.PostingsModal;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.BaseReportPage;
import dk.eazyit.eazyregnskab.web.components.panels.FinanceAccountsPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
@MenuPosition(name = "reports.finance.accounts", parentPage = BalancePage.class, subLevel = 1, topLevelPage = false, topLevel = 0)
public class FinanceAccountsPage extends BaseReportPage {


    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountsPage.class);

    FinanceAccountsPanel financeAccountsPanel;
    public PostingsModal postingsModal;

    public FinanceAccountsPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public FinanceAccountsPage(IModel<ReportObject> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public FinanceAccountsPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(postingsModal = new PostingsModal("FinanceAccountsPage.postingsModal"));
        add(financeAccountsPanel = new FinanceAccountsPanel("FinanceAccountsPage.financeAccounts", new CompoundPropertyModel(getDefaultModel())));
        add(new FinanceAccountsReportForm("FinanceAccountsPage.filters", new CompoundPropertyModel(getDefaultModel()), financeAccountsPanel));


    }


    @Override
    protected void configureComponents() {

    }


}