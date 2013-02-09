package dk.eazyit.eazyregnskab.web.app;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarComponents;
import dk.eazyit.eazyregnskab.web.components.login.LoginNavbarButton;
import dk.eazyit.eazyregnskab.web.components.login.LogoutNavbarButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Eazy IT
 */
public abstract class AppBasePage extends WebPage {


    public AppBasePage() {
        initComponents();
    }

    public AppBasePage(IModel<?> model) {
        super(model);
        initComponents();
    }

    public AppBasePage(PageParameters parameters) {
        super(parameters);
        initComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        Bootstrap.renderHead(response);
    }


    private void initComponents() {

        Navbar topMenu = new Navbar("topMenu");
        topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton(HomePage.class, new ResourceModel("home")).setIconType(IconType.home),
                new NavbarButton(AboutPage.class, new ResourceModel("about")),
                new NavbarButton(ContactPage.class, new ResourceModel("contact")),
                new LoginNavbarButton(LoginPage.class, new ResourceModel("login")),
                new LogoutNavbarButton(WebPage.class, new ResourceModel("logout"))
        ));
        add(topMenu);


    }
}
