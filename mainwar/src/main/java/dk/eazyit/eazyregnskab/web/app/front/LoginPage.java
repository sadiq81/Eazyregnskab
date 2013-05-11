package dk.eazyit.eazyregnskab.web.app.front;

import de.agilecoders.wicket.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends AppBasePage {
    private static final long serialVersionUID = 1L;

    private final static int DURATION = 5;

    private static final Logger LOG = LoggerFactory.getLogger(LoginPage.class);

    public LoginPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public LoginPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public LoginPage(final PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {

        add(new NotificationPanel("feedback"));

        if (parameters != null && parameters.get("error").toBoolean() == true) {
            LOG.debug("Error on login was shown to user");
            getSession().error(new NotificationMessage(new ResourceModel("error.on.login")).hideAfter(Duration.seconds(DURATION)));
        }
        add(new Button("login", new ResourceModel("login")).add(new LoadingBehavior(new StringResourceModel("loading",this,null))));
    }
}
