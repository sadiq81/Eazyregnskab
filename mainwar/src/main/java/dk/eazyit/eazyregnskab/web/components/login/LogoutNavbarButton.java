package dk.eazyit.eazyregnskab.web.components.login;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Trifork
 */
public class LogoutNavbarButton<T> extends LoggedInButton<T> {

    public <T extends Page> LogoutNavbarButton(final Class<T> pageClass, final IModel<String> label) {
        super(pageClass, label);
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
