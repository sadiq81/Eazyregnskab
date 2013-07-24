package dk.eazyit.eazyregnskab.web.components.validators.forms;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.time.Duration;

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

        if (loginService.findAppUserByUsername(formComponent1.getInput()) != null) {
            error(new NotificationMessage(new ResourceModel("username.all.ready.exists")).hideAfter(Duration.seconds(DURATION)));
        }
        if (!Pattern.compile(USERNAME_PATTERN).matcher(formComponent1.getInput()).matches()) {
            error(new NotificationMessage(new ResourceModel("username.not.adequate")).hideAfter(Duration.seconds(DURATION)));
        }
        if (!Objects.equal(formComponent2.getInput(), formComponent3.getInput())) {
            error(new NotificationMessage(new ResourceModel("not.equal.passwords")).hideAfter(Duration.seconds(DURATION)));
        }
        if (!Pattern.compile(PASSWORD_PATTERN).matcher(formComponent2.getInput()).matches()) {
            error(new NotificationMessage(new ResourceModel("password.not.adequate")).hideAfter(Duration.seconds(DURATION)));
        }
    }
}
