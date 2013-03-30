package dk.eazyit.eazyregnskab.web.components.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Trifork
 */
public class LoggedInPage extends AppBasePage {

    final Logger logger = LoggerFactory.getLogger(LoggedInPage.class);

    public LoggedInPage() {
        logger.debug("LoggedInPage created");
    }

    public LoggedInPage(IModel<?> model) {
        super(model);
        logger.debug("LoggedInPage created with model" + model.getObject().toString());
    }

    public LoggedInPage(PageParameters parameters) {
        super(parameters);
        logger.debug("LoggedInPage created with parameters " + parameters.toString());
    }

    @Override
    protected void addToPage() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
