package dk.eazyit.eazyregnskab.web.components.page;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import dk.eazyit.eazyregnskab.web.app.front.AboutPage;
import dk.eazyit.eazyregnskab.web.app.front.ContactPage;
import dk.eazyit.eazyregnskab.web.app.front.HomePage;
import dk.eazyit.eazyregnskab.web.app.front.LoginPage;
import dk.eazyit.eazyregnskab.web.components.button.LoggedOutNavButton;
import dk.eazyit.eazyregnskab.web.components.button.LogoutNavbarButton;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuSetup;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.TopMenuNavBarDropDownButton;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Eazy IT
 */
public abstract class AppBasePage extends WebPage implements IHeaderContributor {

    Navbar topMenu;
    protected final static int DURATION = 5;

    static final Logger LOG = LoggerFactory.getLogger(AppBasePage.class);

    public AppBasePage() {
        super();
        initMenu(null);
    }

    public AppBasePage(IModel<?> model) {
        super(model);
        initMenu(null);
    }

    public AppBasePage(PageParameters parameters) {
        super(parameters);
        initMenu(parameters);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        Bootstrap.renderHead(response);

//        String locale = getSession().getLocale().toString();
//        response.render(new JavaScriptUrlReferenceHeaderItem("/js/feedback/feedback-"+locale+".js", "feedback", true, null, null));
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
        for (final Class<? extends LoggedInPage> clazz : MenuSetup.GetTopLevelList()) {
            MenuPosition menuPosition = (MenuPosition) clazz.getAnnotation(MenuPosition.class);
            topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                    new TopMenuNavBarDropDownButton(
                            new ResourceModel(menuPosition.name().split("\\.")[0])
                            , clazz) {
                        @Override
                        protected Class getLinkSuperClass() {
                            return clazz;
                        }
                    }));
        }

        //Logged in menu buttons
        topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT,
                new LogoutNavbarButton(WebPage.class, new ResourceModel("logout"))
        ));

        add(topMenu);
        addToPage(parameters);
        LOG.trace("Created top menu");

        initMeta();
    }

    private void initMeta() {
        add(new WebMarkupContainer("meta.description").add(new AttributeModifier("content", new ResourceModel("meta.description"))));
        add(new WebMarkupContainer("meta.keywords").add(new AttributeModifier("content", new ResourceModel("meta.keywords"))));
    }

    protected abstract void addToPage(PageParameters pageParameters);

    protected void addToolTipToComponent(Component component, String resourceText) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }

    private boolean isDevelopmentSystem() {
        return System.getProperty("development") != null;
    }

}
