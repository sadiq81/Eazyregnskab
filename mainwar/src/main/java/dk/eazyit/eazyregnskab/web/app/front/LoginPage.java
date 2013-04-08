package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LoginPage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    public LoginPage() {
        super();
    }

    public LoginPage(IModel<?> model) {
        super(model);
    }

    public LoginPage(final PageParameters parameters) {
        super(parameters);

    }

    @Override
    protected void addToPage(PageParameters parameters) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
