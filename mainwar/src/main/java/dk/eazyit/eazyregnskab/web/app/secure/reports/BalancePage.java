package dk.eazyit.eazyregnskab.web.app.secure.reports;

import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author
 */
@MenuPosition(name = "reports.balance", parentPage = BalancePage.class, subLevel = 0)
public class BalancePage extends LoggedInPage {

    public BalancePage() {
    }

    public BalancePage(IModel<?> model) {
        super(model);
    }

    public BalancePage(PageParameters parameters) {
        super(parameters);
    }
}
