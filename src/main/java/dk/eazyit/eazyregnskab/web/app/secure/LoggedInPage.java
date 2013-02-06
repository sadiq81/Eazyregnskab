package dk.eazyit.eazyregnskab.web.app.secure;

import dk.eazyit.eazyregnskab.web.app.AppBasePage;
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
}
