package dk.eazyit.eazyregnskab.web.components.button;

import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Trifork
 */
public class LogoutNavbarButton<T> extends LoggedInButton<T> {

    static final Logger LOG = LoggerFactory.getLogger(LogoutNavbarButton.class);

    public <T extends Page> LogoutNavbarButton(final Class<T> pageClass, final IModel<String> label) {
        super(pageClass, label);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(((EazyregnskabSesssion) getSession()).isSignedIn());
    }
}
