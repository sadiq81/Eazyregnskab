package dk.eazyit.eazyregnskab.web.app.front;

import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.web.components.page.AppBasePage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class SignUpPage extends AppBasePage {



    private static final long serialVersionUID = 1L;
    private CreateInfo createInfo;

    final Logger logger = LoggerFactory.getLogger(SignUpPage.class);

    @SpringBean(name = "loginService")
    private LoginService loginService;

    public SignUpPage() {
        super();
    }

    public SignUpPage(IModel<?> model) {
        super(model);
    }

    public SignUpPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage() {

        SignInForm form = new SignInForm("create_account", new CompoundPropertyModel<CreateInfo>(createInfo = new CreateInfo()));
        add(form);
    }

    public final class SignInForm extends Form<CreateInfo> {

        public SignInForm(final String id, IModel model) {
            super(id, model);

            add(new TextField<String>("username"));
            add(new PasswordTextField("password"));
            add(new PasswordTextField("repeat_password"));
        }

        @Override
        public final void onSubmit() {

            if (createInfo.isUserNameBadFormat()) {
                error("Username not accepted");
            } else if (createInfo.isPasswordBadFormat()) {
                error("Password not accepted");
            } else if (createInfo.arePasswordsNotEquals()) {
                error("Passwords don't match");
            } else {
                loginService.saveUser(createInfo.username, createInfo.password);
                logger.info("Created account " + createInfo.username);
            }
        }
    }

    public class CreateInfo implements Serializable {
        public String username = null;
        public String password = null;
        public String repeat_password = null;

        public boolean isPasswordBadFormat() {
            return password == null || password.length() < 3; // TODO   html chars spaces within etc
        }

        public boolean arePasswordsNotEquals() {
            return password == null || repeat_password == null || !password.equals(repeat_password);
        }

        public boolean isUserNameBadFormat() {
            return username == null || username.length() < 3; // TODO
        }
    }
}
