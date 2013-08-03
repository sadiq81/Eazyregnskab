package dk.eazyit.eazyregnskab.web.components.validators.forms;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.lang.Objects;

import java.util.regex.Pattern;

/**
 * @author
 */
public class CreateAccountFormValidator extends BaseFormValidator {

    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{8,25}$";
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,25})";

    private static final long serialVersionUID = 1L;

    public CreateAccountFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> formComponent1 = getDependentFormComponents()[0];
        final FormComponent<?> formComponent2 = getDependentFormComponents()[1];
        final FormComponent<?> formComponent3 = getDependentFormComponents()[2];
        final FormComponent<?> formComponent4 = getDependentFormComponents()[3];

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
        if (loginService.findAppUserByEmail(formComponent4.getInput()) != null) {
            error(formComponent3, "email.all.ready.in.use");
        }
    }
}
