package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings", parentPage = SettingsPage.class, subLevel = 0)
public class SettingsPage extends LoggedInPage {

    public SettingsPage() {
        super();
    }

    public SettingsPage(IModel<?> model) {
        super(model);
    }

    public SettingsPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage( parameters);
    }
}
