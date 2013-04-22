package dk.eazyit.eazyregnskab.web.app.secure.reports;

import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
@MenuPosition(name = "reports.balance", parentPage = BalancePage.class, subLevel = 0)
public class BalancePage extends LoggedInPage {

    private static final Logger LOG = LoggerFactory.getLogger(BalancePage.class);

    public BalancePage() {
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public BalancePage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public BalancePage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }
}
