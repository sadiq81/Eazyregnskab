package dk.eazyit.eazyregnskab.web.components.validators.forms;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

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
            error(legalIdentification, "legal.identification.to.long");
        }
        if (address.getInput().length() > 200) {
            error(address, "address.to.long");
        }
        if (postalCode.getInput().length() > 20) {
            error(postalCode, "postal.code.to.long");
        }
    }
}
