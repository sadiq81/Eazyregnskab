package dk.eazyit.eazyregnskab.web.components.page;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarComponents;
import dk.eazyit.eazyregnskab.web.app.front.AboutPage;
import dk.eazyit.eazyregnskab.web.app.front.ContactPage;
import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import dk.eazyit.eazyregnskab.web.app.front.LoginPage;
import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import dk.eazyit.eazyregnskab.web.app.secure.reports.BalancePage;
import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import dk.eazyit.eazyregnskab.web.components.login.LoggedInButton;
import dk.eazyit.eazyregnskab.web.components.login.LoggedOutNavButton;
import dk.eazyit.eazyregnskab.web.components.login.LogoutNavbarButton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;


/**
 * @author Eazy IT
 */
public abstract class AppBasePage extends WebPage {

    final Log logger;
    Navbar topMenu;

    public AppBasePage() {
        super();
        logger = LogFactory.getLog(this.getClass());
        initMenu(null);
        logger.debug(this.getClass().getSimpleName().toString() + " was created");
    }

    public AppBasePage(IModel<?> model) {
        super(model);
        logger = LogFactory.getLog(this.getClass());
        initMenu(null);
        logger.debug(this.getClass().getSimpleName().toString() + " was created with model " + model.getObject().toString());
    }

    public AppBasePage(PageParameters parameters) {
        super(parameters);
        logger = LogFactory.getLog(this.getClass());
        initMenu(parameters);
        logger.debug(this.getClass().getSimpleName().toString() + " was created with parameters " + parameters.toString());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        Bootstrap.renderHead(response);
    }


    private void initMenu(PageParameters parameters) {

        topMenu = new Navbar("topMenu");
        topMenu.brandName(new ResourceModel("eazy.regnskab.brand"));

        //Front menu button
        topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new LoggedOutNavButton(HomePage.class, new ResourceModel("home")).setIconType(IconType.home),
                new LoggedOutNavButton(AboutPage.class, new ResourceModel("about")),
                new LoggedOutNavButton(ContactPage.class, new ResourceModel("contact")),
                new LoggedOutNavButton(LoginPage.class, new ResourceModel("login"))

        ));

        //Logged in menu buttons
        topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,

                new LoggedInButton(BaseDataPage.class, new ResourceModel("settings")),
                new LoggedInButton(BookkeepingPage.class, new ResourceModel("bookkeeping")),
                new LoggedInButton(BalancePage.class, new ResourceModel("reports"))
        ));

        //Logged in menu buttons
        topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                new LogoutNavbarButton(WebPage.class, new ResourceModel("logout"))
        ));

        add(topMenu);
        addToPage(parameters);
    }

    protected abstract void addToPage(PageParameters pageParameters);

//    protected void addToolTipToLabel(Label label, String tooltip) {
//        label.add(AttributeModifier.append("rel", "tooltip"));
//        label.add(AttributeModifier.append("data-placement", "top"));
//        label.add(AttributeModifier.append("data-original-title", new ResourceModel(tooltip)));
//    }
}
