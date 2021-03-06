package dk.eazyit.eazyregnskab.web.components.button;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 * Only visible if user is logged in
 */
public class LoggedInButton<T> extends NavbarButton<T> {

    static final Logger LOG = LoggerFactory.getLogger(LoggedInButton.class);

    public <T extends Page> LoggedInButton(final Class<T> pageClass, final IModel<String> label) {
        super(pageClass, new PageParameters(), label);
        LOG.trace("Creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(((EazyregnskabSesssion) getSession()).isSignedIn());
    }

}
