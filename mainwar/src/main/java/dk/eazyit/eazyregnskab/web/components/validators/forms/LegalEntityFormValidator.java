package dk.eazyit.eazyregnskab.web.components.validators.forms;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class LegalEntityFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public LegalEntityFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> legalIdentification = getDependentFormComponents()[0];
        final FormComponent<?> address = getDependentFormComponents()[1];
        final FormComponent<?> postalCode = getDependentFormComponents()[2];

        if (legalIdentification.getInput().length() > 25) {
            error(new NotificationMessage(new ResourceModel("legal.identification.to.long")).hideAfter(Duration.seconds(DURATION)));
        }
        if (address.getInput().length() > 200) {
            error(new NotificationMessage(new ResourceModel("address.to.long")).hideAfter(Duration.seconds(DURATION)));
        }
        if (postalCode.getInput().length() > 20) {
            error(new NotificationMessage(new ResourceModel("postal.code.to.long")).hideAfter(Duration.seconds(DURATION)));
        }
    }
}
