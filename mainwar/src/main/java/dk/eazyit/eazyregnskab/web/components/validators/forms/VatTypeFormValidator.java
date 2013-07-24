package dk.eazyit.eazyregnskab.web.components.validators.forms;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.VatType;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class VatTypeFormValidator extends BaseFormValidator {

    private static final long serialVersionUID = 1L;

    public VatTypeFormValidator(FormComponent... components) {
        super(components);
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
     */
    @Override
    public void validate(Form<?> form) {

        final FormComponent<?> formComponent1 = getDependentFormComponents()[0];
        VatType vatType = (VatType) formComponent1.getForm().getModelObject();

        if (vatType.getId() == 0 && financeAccountService.findVatTypeByNameAndLegalEntity(getCurrentLegalEntity(),
                formComponent1.getInput()) != null) {
            error(new NotificationMessage(new ResourceModel("name.all.ready.exists")).hideAfter(Duration.seconds(DURATION)));
        }
        if (formComponent1.getInput().length() > 50) {
            error(new NotificationMessage(new ResourceModel("name.to.long")).hideAfter(Duration.seconds(DURATION)));
        }
    }
}
