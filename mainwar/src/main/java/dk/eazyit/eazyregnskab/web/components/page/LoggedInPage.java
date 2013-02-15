package dk.eazyit.eazyregnskab.web.components.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Trifork
 */
public class LoggedInPage extends AppBasePage {

    public LoggedInPage() {
    }

    public LoggedInPage(IModel<?> model) {
        super(model);
    }

    public LoggedInPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
