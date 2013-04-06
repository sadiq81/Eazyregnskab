package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author EazyIT
 */
@MenuPosition(name = "chart.of.accounts", parentPage = SettingsPage.class, subLevel = 1)
public class ChartOfAccountsPage extends LoggedInPage {

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
    protected void addToPage() {
        super.addToPage();
    }
}
