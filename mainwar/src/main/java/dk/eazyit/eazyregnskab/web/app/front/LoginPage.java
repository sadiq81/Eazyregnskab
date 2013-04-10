package dk.eazyit.eazyregnskab.web.app.front;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;

public class LoginPage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    private final static int DURATION = 5;

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

        add(new NotificationPanel("feedback"));

        if (parameters != null && parameters.get("error").toBoolean() == true) {
            getSession().error(new NotificationMessage(new ResourceModel("error.on.login")).hideAfter(Duration.seconds(DURATION)));
        }
    }
}
