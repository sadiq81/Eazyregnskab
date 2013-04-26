package dk.eazyit.eazyregnskab.web.components.validators;

import dk.eazyit.eazyregnskab.services.LoginService;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Objects;

import java.util.regex.Pattern;

/**
 * @author
 */
public class CreateAccountFormValidator extends AbstractFormValidator {

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{8,25}$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,25})";

    @SpringBean
    LoginService loginService;

    private static final long serialVersionUID = 1L;

    private final FormComponent<?>[] components;

    public CreateAccountFormValidator(TextField<String> formComponent1
            , PasswordTextField formComponent2
            , PasswordTextField formComponent3) {

        if (formComponent1 == null) {
            throw new IllegalArgumentException("argument formComponent1 cannot be null");
        }
        if (formComponent2 == null) {
            throw new IllegalArgumentException("argument formComponent2 cannot be null");
        }
        if (formComponent3 == null) {
            throw new IllegalArgumentException("argument formComponent3 cannot be null");
        }

        components = new FormComponent[]{formComponent1, formComponent2, formComponent3};
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
     */
    @Override
    public FormComponent<?>[] getDependentFormComponents() {
        return components;
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> formComponent1 = components[0];
        final FormComponent<?> formComponent2 = components[1];
        final FormComponent<?> formComponent3 = components[2];

        if (loginService.findAppUserByUsername(formComponent1.getInput()) != null) {
            error(formComponent1, "username.all.ready.exists");
        }
        if (!Pattern.compile(USERNAME_PATTERN).matcher(formComponent1.getInput()).matches()) {
            error(formComponent2, "username.not.adequate");
        }
        if (!Objects.equal(formComponent2.getInput(), formComponent3.getInput())) {
            error(formComponent2, "not.equal.passwords");
        }
        if (!Pattern.compile(PASSWORD_PATTERN).matcher(formComponent2.getInput()).matches()) {
            error(formComponent2, "password.not.adequate");
        }
    }
}
