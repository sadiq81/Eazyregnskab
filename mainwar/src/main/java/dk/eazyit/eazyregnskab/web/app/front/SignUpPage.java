package dk.eazyit.eazyregnskab.web.app.front;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import dk.eazyit.eazyregnskab.domain.SimpleAppUserInfo;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderPasswordField;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import dk.eazyit.eazyregnskab.web.components.validators.forms.CreateAccountFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.*;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@RequireHttps
public class SignUpPage extends AppBasePage {


    TextField<String> username;
    PasswordTextField password;
    PasswordTextField repeatPassword;
    EmailTextField emailTextField;

    private static final long serialVersionUID = 1L;
    private SimpleAppUserInfo createInfo;
    protected final static int DURATION = 15;

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{8,25}$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,})";

    final Logger LOG = LoggerFactory.getLogger(SignUpPage.class);

    MultiLineLabel whatToDoNow;
    SignUpForm signUpForm;

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

        add(new NotificationPanel("feedback").setOutputMarkupPlaceholderTag(true));

        add(signUpForm = new SignUpForm("create_account", new CompoundPropertyModel<SimpleAppUserInfo>(createInfo = new SimpleAppUserInfo())));

        add(whatToDoNow = (MultiLineLabel) new MultiLineLabel("what.to.do.now", new ResourceModel("what.to.do.now")).setVisibilityAllowed(false).setOutputMarkupPlaceholderTag(true).setEscapeModelStrings(false));

    }

    public final class SignUpForm extends Form<SimpleAppUserInfo> {

        public SignUpForm(final String id, IModel model) {
            super(id, model);
            add(username = (TextField<String>) new PlaceholderTextField<String>("username").setRequired(true).setOutputMarkupId(true));
            add(password = (PasswordTextField) new PlaceholderPasswordField("password").setRequired(true).setOutputMarkupId(true));
            add(repeatPassword = (PasswordTextField) new PlaceholderPasswordField("repeat_password").setRequired(true).setOutputMarkupId(true));
            add(emailTextField = (EmailTextField) new EmailTextField("email").setRequired(true).setOutputMarkupId(true));
            add(new CreateAccountFormValidator(username, password, repeatPassword, emailTextField));
            add(new AjaxButton("save") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    super.onSubmit(target, form);

                    SimpleAppUserInfo userinfo = (SimpleAppUserInfo) form.getModelObject();
                    loginService.createUser(userinfo.getUsername(), userinfo.getPassword(), userinfo.getEmail());
                    getSession().info(new NotificationMessage(new StringResourceModel("account.created.for.user", getPage(), new Model<Serializable>(userinfo))) + " " + userinfo.getUsername());


                    target.add(signUpForm.setVisibilityAllowed(false), whatToDoNow.setVisibilityAllowed(true), getPage().get("feedback"));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    super.onError(target, form);
                    target.add(getPage().get("feedback"));
                }
            }.add(new Label("save.button.label", new ResourceModel("create.account"))));


        }
    }
}

