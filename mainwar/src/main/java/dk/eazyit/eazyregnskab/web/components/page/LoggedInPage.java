package dk.eazyit.eazyregnskab.web.components.page;

import dk.eazyit.eazyregnskab.web.components.navigation.LabeledLinkList;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuSetup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author EazyIT
 */
public class LoggedInPage extends AppBasePage {

    public LoggedInPage() {
        super();
    }

    public LoggedInPage(IModel<?> model) {
        super(model);
    }

    public LoggedInPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage() {

        add(new LabeledLinkList("list", MenuSetup.createSideMenuList(this.getClass().getAnnotation(MenuPosition.class).parentPage())));

    }



}