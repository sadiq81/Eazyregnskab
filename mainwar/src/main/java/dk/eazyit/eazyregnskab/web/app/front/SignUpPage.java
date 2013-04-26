package dk.eazyit.eazyregnskab.web.app.front;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.domain.CreateAppUserInfo;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderPasswordField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import dk.eazyit.eazyregnskab.web.components.validators.forms.CreateAccountFormValidator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpPage extends AppBasePage  {


    TextField<String> username;
    PasswordTextField password;
    PasswordTextField repeatPassword;

    private static final long serialVersionUID = 1L;
    private CreateAppUserInfo createInfo;

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{8,25}$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";

    final Logger LOG = LoggerFactory.getLogger(SignUpPage.class);

    @SpringBean(name = "loginService")
    private LoginService loginService;

    public SignUpPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public SignUpPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public SignUpPage(final PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {

        add(new NotificationPanel("feedback"));

        SignInForm form = new SignInForm("create_account", new CompoundPropertyModel<CreateAppUserInfo>(createInfo = new CreateAppUserInfo()));
        add(form);
    }

    public final class SignInForm extends Form<CreateAppUserInfo> {

        public SignInForm(final String id, IModel model) {
            super(id, model);

            add(username = new PlaceholderTextField<String>("username"));
            add(password = new PlaceholderPasswordField("password"));
            add(repeatPassword = new PlaceholderPasswordField("repeat_password"));
            add(new CreateAccountFormValidator(username, password, repeatPassword));
        }

        @Override
        public final void onSubmit() {
            CreateAppUserInfo userinfo = getModelObject();
            loginService.createUser(userinfo.getUsername(), userinfo.getPassword());
            LOG.info("Created account for " + userinfo.getUsername());
            getSession().info(getString("account.created.for.user") + " " + userinfo.getUsername());
        }


    }
}

