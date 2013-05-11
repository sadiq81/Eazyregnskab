package dk.eazyit.eazyregnskab.web.components.button;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

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
    protected CharSequence getURL() {
        return "/j_spring_security_logout";
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"));
    }
}
