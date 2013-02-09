package dk.eazyit.eazyregnskab.web.components.login;

import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Trifork
 */
public class LogoutNavbarButton<T> extends NavbarButton<T> {

    public <T extends Page> LogoutNavbarButton(final Class<T> pageClass, final IModel<String> label) {
        super(pageClass, new PageParameters(), label);
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
