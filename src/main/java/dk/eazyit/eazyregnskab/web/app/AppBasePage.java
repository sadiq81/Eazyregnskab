package dk.eazyit.eazyregnskab.web.app;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.AbstractNavbarComponent;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarComponents;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.web.components.forms.LoginNavBarForm;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
        topMenu.addComponents();

        topMenu.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton(HomePage.class, new ResourceModel("home")).setIconType(IconType.home),
                new NavbarButton(HomePage.class, new ResourceModel("about")),
                new NavbarButton(HomePage.class, new ResourceModel("contact"))));



        AbstractNavbarComponent tst = new AbstractNavbarComponent(Navbar.ComponentPosition.RIGHT) {
            @Override
            public Component create(String markupId) {
                return new LoginNavBarForm(markupId, new Model<AppUser>(new AppUser()));
            }
        };

        topMenu.addComponents(tst);

        add(topMenu);


    }
}
