package dk.eazyit.eazyregnskab.web.components.navigation.menu;

import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.button.DropDownAutoOpen;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.NavbarDropDownButton;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * @author
 */
public abstract class TopMenuNavBarDropDownButton<T extends LoggedInPage> extends NavbarDropDownButton {

    public TopMenuNavBarDropDownButton(IModel<String> model, Class<? extends LoggedInPage> clazz) {
        super(model);
        add(new DropDownAutoOpen());
    }

    public TopMenuNavBarDropDownButton(IModel<String> model) {
        super(model);
        add(new DropDownAutoOpen());
    }

    @Override
    protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
        List<AbstractLink> linkList = new LinkedList<AbstractLink>();
        for (Class<? extends LoggedInPage> clazz : MenuSetup.createSubMenuList(getLinkSuperClass())) {
            MenuPosition menuPosition = (MenuPosition) clazz.getAnnotation(MenuPosition.class);
            linkList.add(new MenuBookmarkablePageLink(clazz, new ResourceModel(menuPosition.name())));
        }
        return linkList;
    }

    protected abstract Class<T> getLinkSuperClass();

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"));
    }
}
